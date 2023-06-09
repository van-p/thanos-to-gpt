timestamp()
{
	TZ='Asia/Jakarta' date +'[%T]'
}

trigger_thanos()
{
        echo "$(timestamp) :  ========================THANOS-EXECUTION-STARTED========================"
        echo "$(timestamp) :  Thanos Private Token (mandatory) =" $thanos_private_token
        echo "$(timestamp) :  Thanos Trigger Token (mandatory) =" $thanos_trigger_token
        echo "$(timestamp) :  Thanos Project Name (mandatory) =" $thanos_project_name
        echo "$(timestamp) :  Environment (mandatory) =" $environment
        echo "$(timestamp) :  Group Name (optional) =" $thanos_group_name
        echo "$(timestamp) :  Dev Pipeline Details (optional) =" $dev_pipeline_details
        echo "$(timestamp) :  Wait for Job to Finish (optional) =" $wait_for_finish
        check_parameters $thanos_project_name $thanos_private_token $thanos_trigger_token $environment
        thanos_branch_name="master"
        customVariables=""
        if [[ ! -z $thanos_group_name ]]; then
                customVariables="-F variables[groupName]=$thanos_group_name"
        fi

        if [[ ! -z $dev_pipeline_details && "$dev_pipeline_details" != "++" ]]; then
                dev_pipeline_details=$(tr -d ' ' <<< "$dev_pipeline_details")
                customVariables="$customVariables -F variables[buildTag]=$dev_pipeline_details"
        fi
        echo "$(timestamp) :  Custom variables = $customVariables"
        pipe_line_id=$(curl -X POST https://source.abc.io/api/v4/projects/6288/trigger/pipeline -F token=$thanos_trigger_token -F ref=$thanos_branch_name $customVariables 2>/dev/null | jq -r '.id')
        eval "$(curl -X GET https://source.abc.io/api/v4/projects/6288/repository/files/CI-Scripts%2FThanosCommonScript.sh?ref=$thanos_branch_name -H "Private-Token: $thanos_private_token" 2>/dev/null | jq -r '.content' | base64 --decode) $thanos_private_token $pipe_line_id $thanos_ci_job_name $wait_for_finish"
        return_code=$?
        echo "$(timestamp) :  ========================THANOS-EXECUTION-FINISHED========================"
        return $return_code
}

check_parameters()
{
        if [[ -z "$thanos_private_token" || -z "$thanos_trigger_token" ]]; then
                echo "$(timestamp) :  Mandatory parameters(thanos_private_token & thanos_trigger_token) are missing, so exiting..."
                exit 1
        else
                echo "$(timestamp) :  Mandatory parameters are passed correctly, so executing further..."
        fi

        if [[ $environment == "Production" || $environment == "production" ]]; then
                thanos_ci_job_name="${thanos_project_name}-prod-Tests"
        elif [[ $environment == "ProdCanary" || $environment == "prodCanary" ]]; then
                thanos_ci_job_name="${thanos_project_name}-prodcanary-Tests"
        elif [[ $environment == "Sandbox" || $environment == "sandbox" ]]; then
                thanos_ci_job_name="${thanos_project_name}-sbx-Tests"
        else
                thanos_ci_job_name="${thanos_project_name}-stg-Tests"
        fi
        install_jq
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
thanos_trigger_token="$2"
thanos_project_name="$3"
environment="$4"
thanos_group_name="$5"
dev_pipeline_details="$6"
wait_for_finish="$7"
trigger_thanos $thanos_private_token $thanos_trigger_token $thanos_project_name $environment $thanos_group_name $dev_pipeline_details $wait_for_finish