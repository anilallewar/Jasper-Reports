package com.anil.jasper;

/**
 * 
 */
import java.net.URL;

import net.sf.jasperreports.engine.JasperCompileManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author anila
 * 
 */
public class JasperReportsIntro {

	private static Logger logger = LoggerFactory
			.getLogger(JasperReportsIntro.class);

	public static void main(String[] args) {
		try {

			URL url = JasperReportsIntro.class.getClassLoader().getResource(
					"com/anil/jasper/NetworkRequest.jrxml");
			if (url != null) {
				logger.debug("Found resource");
			} else {
				logger.debug("URL is null");
			}

			JasperCompileManager.compileReportToFile(
					"src/com/anil/jasper/NetworkRequest.jrxml",
					"src/com/anil/jasper/NetworkRequest.jasper");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
