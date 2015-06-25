package com.automic.objects;

import java.io.IOException;
import java.util.ArrayList;

import com.automic.utils.ObjectTypeEnum;
import com.uc4.api.objects.FileTransfer;
import com.uc4.api.objects.UC4Object;
import com.uc4.communication.Connection;

public class FileTransfers extends ObjectTemplate{

	public FileTransfers(Connection conn, boolean verbose) {
		super(conn, verbose);
	}
	private ObjectBroker getBrokerInstance(){
		return new ObjectBroker(this.connection,true);
	}

	public ArrayList<UC4Object> getAllJobTransfers() throws IOException{
		ObjectBroker broker = getBrokerInstance();
		return broker.common.getAllObjects(ObjectTypeEnum.JOBF);
	}
	
	public void setJobTransferPriority(UC4Object object, int priority) throws IOException{
		ObjectBroker broker = getBrokerInstance();
		FileTransfer ft = (FileTransfer) object;
		ft.attributes().setPriority(priority);;
		broker.common.saveObject(ft);
	}
	public FileTransfer getFTFromObject(UC4Object object){return (FileTransfer) object;}
	public String getSourceFileFromFT(FileTransfer ft){return ft.settings().getSourceFile();}
	public void setSourceFileFromFT(FileTransfer ft, String SourceFile){ft.settings().setSourceFile(SourceFile);}
	public String getDestinationFileFromFT(FileTransfer ft){return ft.settings().getDestinationFile();}
	public void setDestinationFileFromFT(FileTransfer ft, String DestinationFile){ft.settings().setDestinationFile(DestinationFile);}
	
}