package com.java.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class SensitiveService implements InitializingBean{
	
	private static final Logger logger=LoggerFactory.getLogger(SensitiveService.class);
	
	private class TrieNode{
		//是否为关键词的结尾
		private boolean end=false;
		
		//当前结点的下所有子结点
		private Map<Character, TrieNode> subNodes=new HashMap<>();
		
		public void addSubNode(Character key, TrieNode node){
			subNodes.put(key, node);
		}
		
		public TrieNode getSubNode(Character key){
			return subNodes.get(key);
		}
		
		public boolean isKeywordEnd(){
			return end;
		}
		
		public void setKeywordEnd(boolean end){
			this.end=end;
		}
	}
	
	private TrieNode rootNode=new TrieNode();
	
	private boolean isSymbol(char c){
		int ic=(int)c;
		//东亚文字 0x2E80-0x9FFF
		return !CharUtils.isAsciiAlphanumeric(c) && (ic<0x2E80 || ic>0x9FFF);
	}
	
	//增加关键词
	private void addWord(String lineTxt){
		TrieNode tempNode=rootNode;
		for(int i=0;i<lineTxt.length();i++){
			Character c=lineTxt.charAt(i);
			
			if(isSymbol(c)){
				continue;
			}
			
			TrieNode node=tempNode.getSubNode(c);
			
			if(node==null){
				node=new TrieNode();
				tempNode.addSubNode(c, node);
			}
			
			tempNode=node;
			
			if(i==lineTxt.length()-1){
				tempNode.setKeywordEnd(true);
			}
		}
	}
	
	public String filter(String text){
		if(StringUtils.isBlank(text)){
			return text;
		}
		
		StringBuilder sb=new StringBuilder();
		
		String replacement="***";
		
		TrieNode tempNode=rootNode;
		int begin=0;
		int position=0;
		
		while(position<text.length()){
			char c=text.charAt(position);
		
			if(isSymbol(c)){
				if(tempNode==rootNode){
					sb.append(c);
					++begin;
				}
				++position;
				continue;
			}
			
			tempNode=tempNode.getSubNode(c);
			
			if(tempNode==null){
				sb.append(text.charAt(begin));
				position=begin+1;
				begin=position;
				tempNode=rootNode;	 
			}else if(tempNode.isKeywordEnd()){
				//发现敏感词
				sb.append(replacement);
				position=position+1;
				begin=position;
				tempNode=rootNode;
			}else{
				++position;
			}
		}
		
		sb.append(text.substring(begin));
		return sb.toString();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		try{
			InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
			InputStreamReader read=new InputStreamReader(is);
			BufferedReader bufferedReader=new BufferedReader(read);
			String lineTxt;
			while((lineTxt=bufferedReader.readLine()) !=null){
				addWord(lineTxt.trim());
			}
			read.close();
		}catch(Exception e){
			logger.error("读取敏感词文件失败！"+e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		SensitiveService sensitiveService=new SensitiveService();
		sensitiveService.addWord("色情");
		sensitiveService.addWord("赌博");
		System.out.println(sensitiveService.filter("hi  色       情你好  赌@ 博  "));
	}

}
