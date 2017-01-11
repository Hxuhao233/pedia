package com.pedia.tool;

import java.util.List;

import com.pedia.model.Action;
import com.pedia.model.Comment;
import com.pedia.model.Entry;

public class DetailedEntryData {
	
	private Entry entry;
	private List<CommentData> comments;
	private Action nowContent;
	
	public DetailedEntryData() {
		super();
	}

	public Entry getEntry() {
		return entry;
	}
	public void setEntry(Entry entry) {
		this.entry = entry;
	}
	

	public List<CommentData> getComments() {
		return comments;
	}

	public void setComments(List<CommentData> comments) {
		this.comments = comments;
	}

	public Action getNowContent() {
		return nowContent;
	}

	public void setNowContent(Action nowContent) {
		this.nowContent = nowContent;
	}

	
}
