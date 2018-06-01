#! /bin/sh
echo "Start sx... arguments: $1, $2"
`$1>$2`
echo "Done."
exit 0
