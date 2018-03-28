package com.easycompany.cmm.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import egovframework.rte.fdl.cmmn.exception.EgovBizException;
import egovframework.rte.fdl.cmmn.exception.handler.ExceptionHandler;

public class EasyCompanyExceptionHandler implements ExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(EasyCompanyExceptionHandler.class);

    public void occur(Exception exception, String packageName) {
    	LOGGER.debug(" EasyCompanyExceptionHandler run...............{}", ((EgovBizException) exception).getWrappedException());

        try {
            if (exception instanceof EgovBizException) {
                Exception exx = (Exception) ((EgovBizException) exception).getWrappedException();
                if (exx != null) {
                	LOGGER.debug(" sending a alert mail  is completed ");
                    exx.printStackTrace();
                }
            }
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
