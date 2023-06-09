//Linux mount
sudo mount.cifs //35.247.184.108/RegressionResults/ /home/mukesh.rajput/SharedResults/ rw --verbose -o "username=mukesh.rajput,password=W3lc0me.123,gid=1001,uid=1001,file_mode=0777,dir_mode=0777,sec=ntlm,vers=2.0"

//Linux unmount
sudo umount -f /home/mukesh.rajput/SharedResults



//Mac mount
{
        mount_smbfs //'mukesh.rajput:W3lc0me.123'@35.247.184.108/RegressionResults /Users/automation/SharedResults
}||{
        echo "SharedResults already mounted."
}


//Mac unmount
{
        umount /Users/automation/SharedResults
}||{
        echo "SharedResults already unmounted."
}
