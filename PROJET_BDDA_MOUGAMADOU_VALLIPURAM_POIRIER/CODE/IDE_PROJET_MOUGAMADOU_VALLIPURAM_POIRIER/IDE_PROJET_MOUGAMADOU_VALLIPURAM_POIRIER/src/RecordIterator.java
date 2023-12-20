import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class RecordIterator {
 private TableInfo tabInfo;
 private PageId pageId;
 private int index ;
 private int indexMax;
 private int positionRecord;
 private ByteBuffer buff;
	 
 public RecordIterator(TableInfo tabInfo,PageId pageId) {
	 this.pageId=pageId;
	 this.tabInfo=tabInfo;
	 buff = BufferManager.getInstance().GetPage(pageId);
	 index = 0;
	 buff.position(0);
	 positionRecord= buff.capacity()-8;//4096-8
	 indexMax = buff.getInt(positionRecord);
	 
 }
 public Record GetNextRecord() {
	 if(index==indexMax) {
		 return null;
	 }else {
		 Record rec =  new Record(tabInfo);
		 int intilisationPositionRecord = positionRecord -(8*index);
		 int positonRecord =buff.getInt(intilisationPositionRecord);
		 rec.readFromBuffer(buff, positonRecord);
		 index++;
		 return rec;
	 }
	 
	 
 }
 
 public void close() {
	 if( index==indexMax) {
	 BufferManager.getInstance().FreePage(pageId,1);
	 }
 }
 
 public void Reset() {
	 // on remet à index à 1
	 index =1;
	 
 }
 
 public List<Record> charge() {
	 // parcourir tous les pages pour envoier tous les records
	 buff = BufferManager.getInstance().GetPage(tabInfo.getHeaderPageId());
	 List<Record> lr =new ArrayList();
	 int a = buff.getInt(0);
	 int b = buff.getInt(4);
	 int c = buff.getInt(8);
	 int d = buff.getInt(12);
	 
	 while(a!=-1&&b!=0){
		 // par courir tous la liste et vérifé l'espace Libre 
	      PageId page = new PageId (a,b);
		  // parcouir les tous records
		  RecordIterator rt = new RecordIterator(tabInfo,page);
		  while(rt.index != rt.indexMax) {
			  lr.add(rt.GetNextRecord());
		  }
		  a = rt.buff.getInt(0);
		  b = rt.buff.getInt(4);
	      rt.close();
	      
	}	
	 while(c!=-1&&d!=0){
		 // par courir tous la liste et vérifé l'espace Libre 
	      PageId page = new PageId (c,d);
		  // parcouir les tous records
		  RecordIterator rt = new RecordIterator(tabInfo,page);
		  while(rt.index != rt.indexMax) {
			  lr.add(rt.GetNextRecord());
		  }
		  c = rt.buff.getInt(0);
		  d = rt.buff.getInt(4);
	      rt.close();
 }
	 return lr;
 }
 
}
 
