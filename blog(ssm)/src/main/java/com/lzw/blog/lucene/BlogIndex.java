package com.lzw.blog.lucene;

import com.lzw.blog.entity.Blog;
import com.lzw.blog.util.DateUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @Auther: lzw
 * @Date: 2020/04/28/15:18
 * @Description:
 */
@Component
public class BlogIndex {

	private Directory directory = null;
	private String lucenePath = "D://IdeaProjects//Web(ssm)//Lucene//blogLucene";

	/**
	 * 获取lucene的写入方法
	 */
	private IndexWriter getWtriter() throws Exception {
		directory = FSDirectory.open(Paths.get(lucenePath));
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		return indexWriter;
	}

	//添加索引
	public void addIndex(Blog blog) throws Exception {
		IndexWriter writer = getWtriter();
		Document document = getIndexableFields(blog);
		writer.addDocument(document);
		writer.close();
	}

	/**
	 * 更新索引
	 */
	public void updateIndex(Blog blog) throws Exception {
		IndexWriter writer = getWtriter();
		Document document = getIndexableFields(blog);
		writer.updateDocument(new Term("id", String.valueOf(blog.getId())), document);
		writer.close();
	}

	private Document getIndexableFields(Blog blog) throws ParseException {
		Document document = new Document();
		document.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
		document.add(new TextField("title", blog.getTitle(), Field.Store.YES));
		document.add(new StringField("releaseDate", DateUtil.formatDate(new Date(), "yyyy-MM-dd"), Field.Store.YES));
		document.add(new TextField("content", blog.getContentNoTag(), Field.Store.YES));
		document.add(new StringField("keyWord", blog.getKeyWord(), Field.Store.YES));
		return document;
	}

	/**
	 * 删除索引
	 */
	public void deleteIndex(String blogId) throws Exception {
		IndexWriter writer = getWtriter();
		writer.deleteDocuments(new Term[]{new Term("id", blogId)});
		writer.forceMergeDeletes();
		writer.commit();
		writer.close();
	}

	/**
	 * 搜索
	 */
	public List<Blog> searchBlog(String q) throws Exception {
		List<Blog> blogList = new LinkedList<>();
		directory = FSDirectory.open(Paths.get(lucenePath));
		//获取reader
		IndexReader reader = DirectoryReader.open(directory);
		//获取流
		IndexSearcher searcher = new IndexSearcher(reader);
		//放入查询条件
		BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		QueryParser parser1 = new QueryParser("title", analyzer);
		Query query1 = parser1.parse(q);
		QueryParser parser2 = new QueryParser("content", analyzer);
		Query query2 = parser2.parse(q);
		QueryParser parser3 = new QueryParser("keyWord", analyzer);
		Query query3 = parser3.parse(q);

		booleanQuery.add(query1, BooleanClause.Occur.SHOULD);
		booleanQuery.add(query2, BooleanClause.Occur.SHOULD);
		booleanQuery.add(query3, BooleanClause.Occur.SHOULD);
		//最多返回100条
		TopDocs hits = searcher.search(booleanQuery.build(), 100);
		//高亮搜素
		QueryScorer queryScorer = new QueryScorer(query1);
		Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
		SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");
		Highlighter highlighter = new Highlighter(simpleHTMLFormatter, queryScorer);
		highlighter.setTextFragmenter(fragmenter);
		//遍历
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);
			Blog blog = new Blog();
			blog.setId(Integer.parseInt(doc.get("id")));
			blog.setReleaseDateStr(doc.get("releaseDate"));
			String title = doc.get("title");
			String content = StringEscapeUtils.escapeHtml(doc.get("content"));
			String keyWord = doc.get("keyWord");
			if (title != null) {
				TokenStream tokenStream = analyzer.tokenStream("title", new StringReader(title));
				String hightTitle = highlighter.getBestFragment(tokenStream, title);
				if (StringUtils.isBlank(hightTitle)) {
					blog.setTitle(title);
				} else {
					blog.setTitle(hightTitle);
				}
			}
			if (content != null) {
				TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(content));
				String hightContent = highlighter.getBestFragment(tokenStream, content);
				if (StringUtils.isBlank(hightContent)) {
					if (content.length() <= 200) {
						blog.setContent(content);
					} else {
						blog.setContent(content.substring(0, 200));
					}
				} else {
					blog.setContent(hightContent);
				}
			}
			if (keyWord != null) {
				TokenStream tokenStream = analyzer.tokenStream("keyWord", new StringReader(keyWord));
				String hightkeyWord = highlighter.getBestFragment(tokenStream, keyWord);
				if (StringUtils.isBlank(hightkeyWord)) {
					blog.setKeyWord(keyWord);
				} else {
					blog.setKeyWord(hightkeyWord);
				}
			}
			blogList.add(blog);
		}

		return blogList;
	}

}
