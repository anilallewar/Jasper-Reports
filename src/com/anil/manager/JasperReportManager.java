package com.anil.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.util.SimpleFileResolver;

public abstract class JasperReportManager {
	
	public final static String REPORT_FOR_VIEW_FORMAT = "viewer";
	public final static String REPORT_NAME = "report";
	
	protected HashMap<String, Object> createReportParameters(ServletContext context) throws FileNotFoundException{
		/*
		 * Relative paths from .jrxml files to images (or other resources) do
		 * not work as you suppose they do (i.e. relative to the .jrxml file).
		 * Instead, all relative paths are considered relative to the JVM
		 * current directory.You can customize the way files are resolved by
		 * setting a value for the REPORT_FILE_RESOLVER parameter.
		 * 
		 * The file resolver is simply a class that will be called automatically
		 * by jasper when filling the report, to let the file resolver search
		 * for files in other places.
		 * 
		 * A better option is put the resources in the classpath (maybe under
		 * WEB-INF/classes) as Jasper's default file resolver looks at the
		 * classpath anyways for resource location resolution.
		 */
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		String reportsDirPath = context.getRealPath("/reports/");
		File reportsDir = new File(reportsDirPath);
		if (!reportsDir.exists()) {
			throw new FileNotFoundException(String.valueOf(reportsDir));
		}
		parameters.put(JRParameter.REPORT_FILE_RESOLVER,
				new SimpleFileResolver(reportsDir));

		parameters.put("state", "AK");
		parameters.put("city", "ANCHORAGE");
		//This parameter is utilized to specify the resource specific to the spanish locale
		parameters.put(JRParameter.REPORT_LOCALE, new Locale("es"));
		
		return parameters;
	}
	
	protected boolean isFileToBeSentAsAttachment(String whichFormat){
		if (whichFormat==null || "".equals(whichFormat))
			return true;
		//If non-empty, check and return
		return !(REPORT_FOR_VIEW_FORMAT.equalsIgnoreCase(whichFormat));
	}
}
