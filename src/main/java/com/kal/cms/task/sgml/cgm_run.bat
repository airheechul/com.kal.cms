@ECHO OFF
@ECHO Start CGM TO PNG Convert Process.. arguments: %1, %2, %3, %4, %5
cmd.exe /c java -cp ".;c:/;c:/sgml/ppl_cgm-1.0.0.jar;c:/sgml/commons-io-1.4.jar;c:/sgml/commons-lang-2.4.jar;c:/sgml/jcgm-core-0.2.0.jar;c:/sgml/jcgm-image-0.1.1.jar" com.kal.cms.task.sgml.CgmConvertExecutor %1 %2 %3 %4 %5
@ECHO Done.
