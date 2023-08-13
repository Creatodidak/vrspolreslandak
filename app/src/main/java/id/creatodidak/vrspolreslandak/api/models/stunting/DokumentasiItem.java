package id.creatodidak.vrspolreslandak.api.models.stunting;

import com.google.gson.annotations.SerializedName;

public class DokumentasiItem{

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("link")
	private String link;

	@SerializedName("jenis")
	private String jenis;

	@SerializedName("satker")
	private String satker;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getLink(){
		return link;
	}

	public String getJenis(){
		return jenis;
	}

	public String getSatker(){
		return satker;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getId(){
		return id;
	}
}