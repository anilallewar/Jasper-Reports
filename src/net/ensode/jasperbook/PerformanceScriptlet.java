package net.ensode.jasperbook;

import net.sf.jasperreports.engine.JRAbstractScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformanceScriptlet extends JRAbstractScriptlet {

	private static Logger logger = LoggerFactory
			.getLogger(PerformanceScriptlet.class);
	private long reportInitStartTime;
	private long reportInitEndTime;
	private long pageInitStartTime;
	private long pageInitEndTime;
	private long columnInitStartTime;
	private long columnInitEndTime;
	private long groupInitStartTime;
	private long groupInitEndTime;
	private long detailEvalStartTime;
	private long detailEvalEndTime;

	public void beforeReportInit() throws JRScriptletException {
		reportInitStartTime = System.currentTimeMillis();

	}

	public void afterReportInit() throws JRScriptletException {
		reportInitEndTime = System.currentTimeMillis();

		logger.debug("Report initialization took "
				+ (reportInitEndTime - reportInitStartTime) + " milliseconds.");
	}

	public void beforePageInit() throws JRScriptletException {
		pageInitStartTime = System.currentTimeMillis();

	}

	public void afterPageInit() throws JRScriptletException {
		pageInitEndTime = System.currentTimeMillis();
		Integer pageNum = (Integer) getVariableValue("PAGE_NUMBER");

		logger.debug("Page " + pageNum + " initialization took "
				+ (pageInitEndTime - pageInitStartTime) + " milliseconds.");
	}

	public void beforeColumnInit() throws JRScriptletException {
		columnInitStartTime = System.currentTimeMillis();

	}

	public void afterColumnInit() throws JRScriptletException {
		columnInitEndTime = System.currentTimeMillis();
		Integer columnNum = (Integer) getVariableValue("COLUMN_NUMBER");

		logger.debug("Column " + columnNum + " initialization took "
				+ (columnInitEndTime - columnInitStartTime) + " milliseconds.");

	}

	public void beforeGroupInit(String groupName) throws JRScriptletException {
		groupInitStartTime = System.currentTimeMillis();
	}

	public void afterGroupInit(String groupName) throws JRScriptletException {
		groupInitEndTime = System.currentTimeMillis();

		logger.debug("Group " + groupName + " initialization took "
				+ (groupInitEndTime - groupInitStartTime) + " milliseconds.");
	}

	public void beforeDetailEval() throws JRScriptletException {
		detailEvalStartTime = System.currentTimeMillis();
	}

	public void afterDetailEval() throws JRScriptletException {
		detailEvalEndTime = System.currentTimeMillis();

		logger.debug("Detail evaluation took "
				+ (detailEvalEndTime - detailEvalStartTime) + " milliseconds.");
	}
}
