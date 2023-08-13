package id.creatodidak.vrspolreslandak.api.models.stunting;

import java.util.List;

public class DataKms{
	private List<DataperkembanganItem> dataperkembangan;
	private boolean found;
	private List<MimtuItem> mimtu;
	private String nama;
	private List<MbbtbItem> mbbtb;
	private List<MtbuItem> mtbu;
	private List<MbbuItem> mbbu;

	public List<DataperkembanganItem> getDataperkembangan(){
		return dataperkembangan;
	}

	public boolean isFound(){
		return found;
	}

	public List<MimtuItem> getMimtu(){
		return mimtu;
	}

	public String getNama(){
		return nama;
	}

	public List<MbbtbItem> getMbbtb(){
		return mbbtb;
	}

	public List<MtbuItem> getMtbu(){
		return mtbu;
	}

	public List<MbbuItem> getMbbu(){
		return mbbu;
	}
}