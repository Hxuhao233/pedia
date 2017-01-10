package com.pedia.tool;

import java.util.List;

import com.pedia.model.Comment;
import com.pedia.model.Entry;
import com.pedia.model.Label;

public class DetailedEntryData {
	
	private Entry entry;
	private String publisher;
	private List<Label> labels;
	private List<CommentData> comments;
	
	public DetailedEntryData() {
		super();
	}

	public Entry getEntry() {
		return entry;
	}
	public void setEntry(Entry entry) {
		this.entry = entry;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public List<Label> getLabels() {
		return labels;
	}
	public void setLabels(List<Label> labels) {
		this.labels = labels;
	}

	public List<CommentData> getComments() {
		return comments;
	}

	public void setComments(List<CommentData> comments) {
		this.comments = comments;
	}

	
}
