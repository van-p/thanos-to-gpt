timestamp()
{
	TZ='Asia/Jakarta' date +'[%T]'
}

check_pipleline_status()
{
        if [ $pipe_line_id == "null" ]; then
                echo "$(timestamp) :  Unable to trigger the pipeline, please check your access rights!"
                return 1
        else
                echo "$(timestamp) :  Pipeline triggered successfully, now triggering the CI Job..."
        fi
        echo "$(timestamp) :  Pipeline Number =" $pipe_line_id
        echo "$(timestamp) :  Job to be triggered =" $thanos_ci_job_name
        job_id_info=$(curl -X GET https://source.abc.io/api/v4/projects/6288/pipelines/$pipe_line_id/jobs?per_page=100 -H "Private-Token: $thanos_private_token" 2>/dev/null | jq -r '.[]|"\(.id) \(.name)"' | grep $thanos_ci_job_name)
        echo "$(timestamp) :  Info =" $job_id_info
        if [[ -z "$job_id_info" ]]; then
                echo Unable to find job with name: $thanos_ci_job_name, please check your access rights!
                return 1
        fi
        regular_expression="^([^-]+) (.*)$"
        [[ "$job_id_info" =~ $regular_expression ]] && job_id="${BASH_REMATCH[1]}"
        echo "$(timestamp) :  Job Id =" $job_id
        sleep 5
        name=$(curl -X POST https://source.abc.io/api/v4/projects/6288/jobs/$job_id/play -H "Cache-Control: no-cache" -H "Private-Token: $thanos_private_token" 2>/dev/null | jq -r '.name')
        echo "$(timestamp) :  Triggered Job Name =" $name
        echo "$(timestamp) :  https://source.abc.io/qa/thanos/-/jobs/"$job_id
        if [[ $wait_for_finish == "no" ]]; then
        		return 0
        fi
        if [[ -z "$name" ]]; then
          echo "$(timestamp) : Job is not started"
          return 2
        fi
        echo "$(timestamp) :  Waiting & polling for Job to finish..."
        sleep 30
        job_finished_at=$(curl -X GET https://source.abc.io/api/v4/projects/6288/jobs/$job_id -H "Cache-Control: no-cache" -H "Private-Token: $thanos_private_token" 2>/dev/null | jq -r '.finished_at')
        count=1
        until [ $job_finished_at != "null" ]; do
                if [ $count -eq 70 ]; then
                        result=$(curl -X POST https://source.abc.io/api/v4/projects/6288/jobs/$job_id/cancel -H "Cache-Control: no-cache" -H "Private-Token: $thanos_private_token" 2>/dev/null)
                        echo "$(timestamp) :  Job was taking longer time than 35 minutes, so CANCELLED it... Please try again."
                        return 2
                fi
                sleep 30
                job_finished_at=$(curl -X GET https://source.abc.io/api/v4/projects/6288/jobs/$job_id -H "Cache-Control: no-cache" -H "Private-Token: $thanos_private_token" 2>/dev/null | jq -r '.finished_at')
                let count+=1
                if [ $job_finished_at == "null" ]; then
                        echo "$(timestamp) :  is_job_finished = false"
                else
                        echo "$(timestamp) :  is_job_finished = true"
                fi
        done
        
        fullLogs=$(curl -X GET https://source.abc.io/api/v4/projects/6288/jobs/$job_id/trace -H "Cache-Control: no-cache" -H "Private-Token: $thanos_private_token" 2>/dev/null)
        delimiter="~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
        s="$fullLogs$delimiter"
        array=();  
        while [[ $s ]];  
        do  
        array+=( "${s%%"$delimiter"*}" );  
        s=${s#*"$delimiter"};  
        done;  
        echo "$(timestamp) :  ${array[1]}"

        test_status=$(curl -X GET https://source.abc.io/api/v4/projects/6288/jobs/$job_id -H "Cache-Control: no-cache" -H "Private-Token: $thanos_private_token" 2>/dev/null | jq -r '.status')
        echo "$(timestamp) :  Full Logs : https://source.abc.io/qa/thanos/-/jobs/"$job_id
        if [ $test_status == "success" ]; then
                return 0
        else
                return 1
        fi
}
check_pipleline_status $thanos_private_token $pipe_line_id $thanos_ci_job_name $wait_for_finish