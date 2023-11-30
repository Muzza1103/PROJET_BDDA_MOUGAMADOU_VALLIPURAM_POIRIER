public class PageId {
	private int fileIdx;
	private int pageIdx;
	private int nbreAcces;
	private int octetTaille;

	public PageId(int fileIdx, int pageIdx){
		this.fileIdx = fileIdx;
		this.pageIdx = pageIdx;
		this.nbreAcces = 0;
		this.octetTaille = 0;
	}
	
	public int getFileIdx() {
		return fileIdx;
	}

	public int getPageIdx() {
		return pageIdx;
	}

	public boolean equals(Object obj){
    PageId p = (PageId) obj;
    return fileIdx == p.getFileIdx() && pageIdx == p.getPageIdx();
}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("F"+fileIdx+", Page : "+pageIdx);
		return sb.toString();
	}
	
	public void incrNbreAcces() {
		nbreAcces++;
	}
	
	public int getNbreAcces() {
		return nbreAcces;
	}
	
	public void setOctetTaille(int octetTaille) {
		this.octetTaille = octetTaille;
	}
	
	public int getOctetTaille() {
		return octetTaille;
	}
}

