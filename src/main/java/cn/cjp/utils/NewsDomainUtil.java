package cn.cjp.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.cjp.spider.chinanews.news.domain.ChinanewsDomain;

public class NewsDomainUtil {

	private ChinanewsDomain news;
	//保存到的文件名
	private String file_name = "D:/__JAVA__/__LOG__/cjp-spider/news/";
	
	public NewsDomainUtil(ChinanewsDomain news)
	{
		this.news = news;
	}
	
	public String buildHtmlFileName()
	{
		String f_name = file_name + "_" + this.news.getId() + ".jsp";
		return f_name;
	}
	
	public String buildHtmlFileCont() throws Exception
	{
		InputStream in = new FileInputStream(new File("./WebRoot/index.html"));
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String s = "",t = "";
		while((t=br.readLine())!=null)
		{
			s += t;
		}
		//处理内容
		s = this.replaceStr(s, "$news.title", news.getTitle());
		s = this.replaceStr(s, "$news.pubDate", news.getPubTime()+"");
		s = this.replaceStr(s, "$news.sourceFrom", news.getSourceFrom());
		s = this.replaceStr(s, "$news.urlFrom", news.getUrl());
		s = this.replaceStr(s, "$news.id", news.getId()+"");
		s = this.replaceStr(s, "$news.content", news.getContent());
		
		return s;
	}
	
	public void buildHTML() throws Exception
	{
		String htmlFileName = buildHtmlFileName();
		String htmlFileCont = buildHtmlFileCont();
		
		// 先生成html文件
		File htmlFile = new File(htmlFileName);
		// 设置路径
		if( !htmlFile.exists() )
		{
			htmlFile.createNewFile();
		}
		
		FileOutputStream fout = new FileOutputStream(htmlFile, true);
		
		fout.write( htmlFileCont.getBytes() );
		
		fout.close();
	}
	
	public String replaceStr(String source,String from,String to)
	{
		int i = source.indexOf(from);
		if(i!=-1)
		{
			String end = source.substring(0, i);
			end += to;
			end += source.substring(i+from.length());
			return end;
		}
		return source;
	}
	
	public static void main(String []args) throws Exception
	{
		ChinanewsDomain news = new ChinanewsDomain();
		news.setTitle("title");
		news.setContent("content");
		NewsDomainUtil nu = new NewsDomainUtil(news);
		nu.buildHTML();
	}
}
