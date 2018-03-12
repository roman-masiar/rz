package cz.leveland.robzone.api;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import cz.leveland.appbase.util.HttpConnectionTool;
import cz.leveland.appbase.util.HttpResponse;

public abstract class AbstractApiSource implements ApiSource{
	
	
	public static final int TYPE_IMPORT = 1;
	public static final int TYPE_UPDATE = 2;
	
	protected String fileRoot, importPath, url;
	
	
	
	public AbstractApiSource(String fileRoot, String importPath) {		
		this.fileRoot = fileRoot;
		this.importPath = importPath;
		paths.put(TYPE_IMPORT, importPath);
	}
	
	public AbstractApiSource(String url) {		
		this.url = url;		
		paths.put(TYPE_IMPORT, url);
	}
	
	protected Map<Integer,String> paths = new HashMap<Integer,String>();
	
	protected String getPath(int type) throws APIConnectionException {
		
		if (!paths.containsKey(type))
			throw new APIConnectionException();
		
		return paths.get(type);
	}

	
	@Override
	public void addPath(int type, String path) {
		paths.put(type, path);
	}
	
	
	protected String readFile(int type) throws APIConnectionException {
		
		String fileName = fileRoot + getPath(type);
		try {
			try(FileInputStream inputStream = new FileInputStream(fileName)) {     
			    String creditsString = IOUtils.toString(inputStream);
			    return creditsString;
			}
			
		}catch(Exception e) {			
			throw new APIConnectionException(); 
		}
	}

	protected FileInputStream getInputStream(int type) throws APIConnectionException {
		
		String fileName = fileRoot + getPath(type);
		try {
			try(FileInputStream inputStream = new FileInputStream(fileName)) {     				
				return inputStream;
			}
			
		} catch(Exception e) {
			throw new APIConnectionException(); 
		}
	}
	
	public String getDataFromUrl(int type) throws APIConnectionException {
		
		String path = getPath(type); 
		try {
			HttpResponse result = HttpConnectionTool.sendGetHttp(path);
			if (result.getResponseCode() != 200) 
				throw new APIConnectionException();
			return result.getData();
		}catch(Exception e) {			
			throw new APIConnectionException();
		}
	}
	

	
	

}
