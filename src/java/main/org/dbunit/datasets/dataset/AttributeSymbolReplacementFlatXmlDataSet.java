package org.dbunit.datasets.dataset;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.xml.sax.InputSource;


public class AttributeSymbolReplacementFlatXmlDataSet extends FlatXmlDataSet {

	public AttributeSymbolReplacementFlatXmlDataSet(
			FlatXmlProducer flatXmlProducer) throws DataSetException {
		super(flatXmlProducer);
	}
	
	
    public AttributeSymbolReplacementFlatXmlDataSet(Map<String,String> replacements,Reader xmlReader, boolean dtdMetadata, boolean columnSensing, boolean caseSensitiveTableNames)
    throws IOException, DataSetException
    {
        super(new AttributeSymbolReplacementFlatXmlProducer(new InputSource(xmlReader), replacements,dtdMetadata, columnSensing, caseSensitiveTableNames));
    }

}
