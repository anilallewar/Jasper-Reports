package net.ensode.jasperbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ensode.datasource.JasperBeanCollectionDS;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class EmptyDSReportFill {
	private static Logger logger = LoggerFactory
	.getLogger(EmptyDSReportFill.class);
	
	public void fillReport(String reportName) {
		String reportDirectory = "reports";
		try {
			logger.debug("Filling report..." + reportName);

			JRDataSource dataSource = null;

			if ("BeanDSGroupedReport".equalsIgnoreCase(reportName)) {
				dataSource = new JasperBeanCollectionDS()
						.createReportDataSource();
			} else {
				dataSource = new JREmptyDataSource();
			}

			JasperPrint report = JasperFillManager.fillReport(reportDirectory
					+ "/" + reportName + ".jasper", null, dataSource);
			JasperViewer jsViewer = new JasperViewer(report);
			jsViewer.setVisible(true);

			logger.debug("Done!");
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * Use the following values for getting different reports 1.
		 * ScriptletVariableModificationReport - For scriptlet modifying
		 * variable value at runtime a. Uses the
		 * "net.ensode.jasperbook.PerformanceScriptlet" class 2.
		 * ScriptletDemoReport - For Scriptlet demo a. Check out the console for
		 * the log statements b. Reports can call use-defined methods in
		 * scriptlet class by using the built-in parameter REPORT_SCRIPTLET. For
		 * example, if our scriptlet has a method called foo(), a report could
		 * access it by using the syntax $P{REPORT_SCRIPTLET}.foo(). c. Uses the
		 * "net.ensode.jasperbook.ReportVariableModificationScriptlet" class 3.
		 * BeanDSGroupedReport - For beans containing collections example
		 */
		if (args == null || args.length == 0) {
			logger.debug("Passed argument list is null");
			args = new String[] { "GraphicsDemoReport" };
		}
		new EmptyDSReportFill().fillReport(args[0]);
	}
}
