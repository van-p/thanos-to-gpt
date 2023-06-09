{
	umount $1
	ls $1
}||{
	echo "Unable to unmount the 'SharedResults'!"
}