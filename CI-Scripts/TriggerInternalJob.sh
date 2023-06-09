trigger_internal_job()
{
        echo "Job to be Triggered = $thanos_job_name"
        install_jq
        job_id_info=$(curl -X GET https://source.abc.io/api/v4/projects/6288/pipelines/$CI_PIPELINE_ID/jobs?per_page=100 -H "Private-Token: $thanos_private_token" 2>/dev/null | jq -r '.[]|"\(.id) \(.name)"' | grep "$thanos_job_name")
        if [[ -z "$job_id_info" ]]; then
                echo Unable to find job with name: "$thanos_job_name", please check your access rights!
                return 1
        fi
        regular_expression="^([^-]+) (.*)$"
        [[ "$job_id_info" =~ $regular_expression ]] && job_id="${BASH_REMATCH[1]}"
        web_url=$(curl -X POST https://source.abc.io/api/v4/projects/6288/jobs/$job_id/play -H "Cache-Control: no-cache" -H "Private-Token: $thanos_private_token" 2>/dev/null | jq -r '.web_url')
        if [[ -z "$web_url" ]]; then
        		echo "Unable to find job with name: $thanos_job_name, please check your access rights!"
        else 
		        echo "Job Triggered successfully -> $web_url"
		fi
}

install_jq()
{
        {
                jq --version
        }||{
                echo 'jq' is not installed in this machine, so installing now...
                mkdir -p ~/bin && curl -sSL -o ~/bin/jq https://github.com/stedolan/jq/releases/download/jq-1.5/jq-linux64 && chmod +x ~/bin/jq
                export PATH=$PATH:~/bin
                jq --version
        }
}

thanos_private_token="$1"
thanos_job_name="$2"
trigger_internal_job $thanos_private_token $thanos_job_name
