{
	mount_smbfs //'Administrator:Midtrans%40QA'@52.221.7.215/RegressionResults $1
	ls $1
}||{
	echo "Unable to mount the 'SharedResults'!"
}