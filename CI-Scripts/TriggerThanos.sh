validate_and_trigger_job()
{
         if [[ -z $environment ]]; then
                echo "Triggering another Job in the same pipeline..."
                trigger_internal_job "$thanos_private_token" "$thanos_trigger_token"
        else
                echo "Executing Actual Thanos..."
                trigger_actual_thanos "$thanos_private_token" "$thanos_trigger_token" "$thanos_project_name" "$environment" "$thanos_group_name" "$dev_pipeline_details" "$wait_for_finish"
        fi
}

#This function is optional to put, this can be used to trigger another job, just pass teh variables and hardcode dev_project_id for your dev project
trigger_internal_job()
{
        dev_project_id="12611"
        project_access_token="$thanos_private_token"
        thanos_job_name="$thanos_trigger_token"
        echo "Job to be Triggered = $thanos_job_name"
        install_jq
        job_id_info=$(curl -X GET https://source.abc.io/api/v4/projects/$dev_project_id/pipelines/$CI_PIPELINE_ID/jobs?per_page=100 -H "Private-Token: $project_access_token" 2>/dev/null | jq -r '.[]|"\(.id) \(.name)"' | grep "$thanos_job_name")
        if [[ -z "$job_id_info" ]]; then
                echo Unable to find job with name: "$thanos_job_name", please check your access rights!
                return 1
        fi
        regular_expression="^([^-]+) (.*)$"
        [[ "$job_id_info" =~ $regular_expression ]] && job_id="${BASH_REMATCH[1]}"
        web_url=$(curl -X POST https://source.abc.io/api/v4/projects/$dev_project_id/jobs/$job_id/play -H "Cache-Control: no-cache" -H "Private-Token: $project_access_token" 2>/dev/null | jq -r '.web_url')
        echo "Job Triggered successfully! $web_url"
}

trigger_actual_thanos()
{
        install_jq
        eval "$(curl -X GET https://source.abc.io/api/v4/projects/6288/repository/files/CI-Scripts%2FRunThanosTests.sh?ref=master -H "Private-Token: $thanos_private_token" 2>/dev/null | jq -r '.content' | base64 --decode) "$thanos_private_token" "$thanos_trigger_token" "$thanos_project_name" "$environment" "$thanos_group_name" "$dev_pipeline_details" "$wait_for_finish""
        return $?
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

#To enable for your project, just replace "Rhea" with the name of your project in line 59 and pass these 4 parameters from CLI:
#1. thanos_private_token - Mandatory
#2. thanos_trigger_token - Mandatory
#3. environment - Mandatory (Staging, Sandbox, Production)
#4. thanos_group_name - Optional (regression, production, apiCases etc.)

thanos_private_token="$1"
thanos_trigger_token="$2"
environment="$3"
thanos_group_name="$4"
thanos_project_name="Rhea"
wait_for_finish="yes"
dev_pipeline_details="${CI_PROJECT_TITLE}+${CI_COMMIT_BRANCH}+${CI_PIPELINE_URL}"
validate_and_trigger_job "$thanos_private_token" "$thanos_trigger_token" "$thanos_project_name" "$environment" "$thanos_group_name" "$dev_pipeline_details" "$wait_for_finish"
