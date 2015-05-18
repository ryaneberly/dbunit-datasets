package org.dbunit.datasets.dataset;


	import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

	import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.datasets.dataset.tokens.MapTokenResolver;
import org.dbunit.datasets.dataset.tokens.TokenReplacingReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

	/**
	 * Builder for the creation of {@link FlatXmlDataSet} instances.
	 * 
	 * @see FlatXmlDataSet
	 * @author gommma (gommma AT users.sourceforge.net)
	 * @author Last changed by: $Author$
	 * @version $Revision$ $Date$
	 * @since 2.4.7
	 */
	public class AttributeSymbolReplacementFlatXmlDataSetBuilder {
		
		private Map<String, String> replacements;
		
	    /**
	     * Logger for this class
	     */
	    private static final Logger logger = LoggerFactory.getLogger(AttributeSymbolReplacementFlatXmlDataSetBuilder.class);

	    /**
	     * Whether or not DTD metadata is available to parse via a DTD handler. Defaults to {@value}
	     */
	    private boolean dtdMetadata = true;
	    
	    /**
	     * Since DBUnit 2.3.0 there is a functionality called "column sensing" which basically
	     * reads in the whole XML into a buffer and dynamically adds new columns as they appear.
	     * Defaults to {@value}
	     */
	    private boolean columnSensing = false;
	    /**
	    * Whether or not the created dataset should use case sensitive table names
	    * Defaults to {@value}
	    */
	    private boolean caseSensitiveTableNames = false;
	    
	    
	    public AttributeSymbolReplacementFlatXmlDataSetBuilder()
	    {
			this.replacements = new HashMap<String, String>();
	    }
	    public AttributeSymbolReplacementFlatXmlDataSetBuilder(String ... keyAndValues)
	    {
			this.replacements = new HashMap<String, String>();
			for(int i=0; i<keyAndValues.length-1; i+=2){
				replacements.put(keyAndValues[i], keyAndValues[i+1]);
			}
	    }
	    public AttributeSymbolReplacementFlatXmlDataSetBuilder replaces(String s1, String s2) {
	    	replacements.put(s1, s2);
	        return this;
	    }
	    

	    /**
	     * Sets the flat XML input source from which the {@link FlatXmlDataSet} is to be built
	     * @param xmlInputFile The flat XML input as {@link File}
	     * @return The created {@link FlatXmlDataSet}
	     * @throws DataSetException
	     * @throws IOException  
	     */
	    public AttributeSymbolReplacementFlatXmlDataSet build(File xmlInputFile) throws DataSetException,IOException
	    {
	        //URL xmlInputUrl = xmlInputFile.toURL();
	        //InputSource inputSource = createInputSourceFromUrl(xmlInputUrl);
	        return build(new FileReader(xmlInputFile));
	    }
	    
	    /**
	     * Sets the flat XML input source from which the {@link FlatXmlDataSet} is to be built
	     * @param xmlInputUrl The flat XML input as {@link URL}
	     * @return The created {@link FlatXmlDataSet}
	     * @throws DataSetException 
	     * @throws IOException 
	     */
	    public AttributeSymbolReplacementFlatXmlDataSet build(URL xmlInputUrl) throws DataSetException, IOException
	    {
	    	return build(xmlInputUrl.openStream());
	    }
	    
	    /**
	     * Sets the flat XML input source from which the {@link FlatXmlDataSet} is to be built
	     * @param xmlReader The flat XML input as {@link Reader}
	     * @return The created {@link FlatXmlDataSet}
	     * @throws DataSetException 
	     * @throws IOException 
	     */
	    public AttributeSymbolReplacementFlatXmlDataSet build(Reader xmlReader) throws DataSetException, IOException
	    {
	    	if(replacements != null){
	    		xmlReader = new TokenReplacingReader(xmlReader, new MapTokenResolver(replacements));
	    	}
	        return new AttributeSymbolReplacementFlatXmlDataSet(replacements,xmlReader,dtdMetadata,columnSensing,caseSensitiveTableNames);
	    }

	    /**
	     * Sets the flat XML input source from which the {@link FlatXmlDataSet} is to be built
	     * @param xmlInputStream The flat XML input as {@link InputStream}
	     * @return The created {@link FlatXmlDataSet}
	     * @throws DataSetException 
	     * @throws IOException 
	     */
	    public AttributeSymbolReplacementFlatXmlDataSet build(InputStream xmlInputStream) throws DataSetException, IOException
	    {
	    	return build(new InputStreamReader(xmlInputStream));
	    }
	    
	    public boolean isDtdMetadata() {
	        return dtdMetadata;
	    }

	    /**
	     * Whether or not DTD metadata is available to parse via a DTD handler.
	     * @param dtdMetadata
	     * @return this
	     */
	    public AttributeSymbolReplacementFlatXmlDataSetBuilder setDtdMetadata(boolean dtdMetadata) {
	        this.dtdMetadata = dtdMetadata;
	        return this;
	    }

	    public boolean isColumnSensing() {
	        return columnSensing;
	    }

	    /**
	     * Since DBUnit 2.3.0 there is a functionality called "column sensing" which basically
	     * reads in the whole XML into a buffer and dynamically adds new columns as they appear.
	     * @param columnSensing
	     * @return this
	     */
	    public AttributeSymbolReplacementFlatXmlDataSetBuilder setColumnSensing(boolean columnSensing) {
	        this.columnSensing = columnSensing;
	        return this;
	    }

	    public boolean isCaseSensitiveTableNames() {
	        return caseSensitiveTableNames;
	    }

	    /**
	     * Whether or not the created dataset should use case sensitive table names
	     * @param caseSensitiveTableNames
	     * @return this
	     */
	    public AttributeSymbolReplacementFlatXmlDataSetBuilder setCaseSensitiveTableNames(boolean caseSensitiveTableNames) {
	        this.caseSensitiveTableNames = caseSensitiveTableNames;
	        return this;
	    }

	    @Override
		public String toString()
	    {
	        StringBuffer sb = new StringBuffer();
	        sb.append(getClass().getName()).append("[");
	        sb.append("dtdMetadata=").append(dtdMetadata);
	        sb.append(", columnSensing=").append(columnSensing);
	        sb.append(", caseSensitiveTableNames=").append(caseSensitiveTableNames);
	        sb.append("]");
	        return sb.toString();
	    }
	}