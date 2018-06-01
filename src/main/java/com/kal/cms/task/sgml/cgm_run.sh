#! /bin/sh
echo "Start CGM TO PNG Convert Process.. arguments: $1, $2, $3, $4, $5"
/was/IBM/WebSphere/AppServer/java/bin/java -cp ".:/was/apps/sgml/:/was/apps/sgml/ppl_cgm-1.0.0.jar:/was/apps/sgml/commons-io-1.4.jar:/was/apps/sgml/commons-lang-2.4.jar:/was/apps/sgml/jcgm-image-0.1.1.jar:/was/apps/sgml/jcgm-core-0.2.0.jar:" com.kal.cms.task.sgml.CgmConvertExecutor $1 $2 $3 $4 $5
echo "Done."
exit 0
