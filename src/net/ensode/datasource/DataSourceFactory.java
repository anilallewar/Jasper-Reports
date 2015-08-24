package net.ensode.datasource;

import java.util.HashMap;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;

public class DataSourceFactory {
	public static HashMap<String, String> dataSourceMapping = new HashMap<String, String>();
	
	static{
		dataSourceMapping.put("BeanArrayDataSource", "net.ensode.datasource.JasperBeanArrayDS");
		dataSourceMapping.put("BeanCollectionDataSource", "net.ensode.datasource.JasperBeanCollectionDS");
		dataSourceMapping.put("CsvDataSource","net.ensode.datasource.JasperCSVDS");
		dataSourceMapping.put("CustomDataSource","net.ensode.datasource.JasperCustomDataSource");
		dataSourceMapping.put("MapArrayDataSource","net.ensode.datasource.JasperMapArrayDS");
		dataSourceMapping.put("MapCollectionDataSource","net.ensode.datasource.JasperMapCollectionDS");
		dataSourceMapping.put("SwingTableModel","net.ensode.datasource.TableModelReport");
		dataSourceMapping.put("XMLDataSource","net.ensode.datasource.JasperXmlDS");
		dataSourceMapping.put("JasperResultSetDataSource","net.ensode.datasource.JasperResultSetDS");
		dataSourceMapping.put("JasperFrameTestDataSource","net.ensode.datasource.JasperFrameTestDS");
	}
	
	/**
	 * Private constructor so that all the objects are available through static
	 * methods only
	 */
	private DataSourceFactory(){}
	
	/**
	 * This method will return the Jasper data source object based on the passed typeOfDataSource parameter
	 * 
	 * @param typeOfDataSource
	 * @param context
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public static JRDataSource getDataSource(String typeOfDataSource, ServletContext context) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		
		JRDataSource dataSource = null;
		if (typeOfDataSource==null || "".equals(typeOfDataSource.trim())){
			dataSource = new JREmptyDataSource();
		}
		
		String mappedDataSource = dataSourceMapping.get(typeOfDataSource.trim());
		
		if (mappedDataSource==null)
			dataSource = new JREmptyDataSource();
		else{
			IJasperDataSource dataSourceClass = (IJasperDataSource)Class.forName(mappedDataSource).newInstance();
			dataSource = dataSourceClass.createReportDataSource(context);
		}
		
		return dataSource;
	}
}
