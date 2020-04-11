package com.myshare.code.lucene;

import com.myshare.code.entity.ArcType;
import com.myshare.code.entity.Article;
import com.myshare.code.entity.User;
import com.myshare.code.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 课程资料索引类
 */

@Component
public class ArticleIndex {

	private Directory dir = null;
	@Value("${lucenePath}")
	private String lucenePath;

	/**
	 * 获取IndexWriter实例
	 * @return
	 */
	private IndexWriter getWriter() throws Exception {
		dir = FSDirectory.open(Paths.get(lucenePath));
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(dir,iwc);
		return writer;
	}

	/**
	 * 添加资源索引
	 */
	public void addIndex(Article article){
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		try {
			IndexWriter writer = getWriter();
			Document doc = getIndexableFields(article);
			writer.addDocument(doc);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 修改资源索引
	 */
	public void updateIndex(Article article){
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		try {
			IndexWriter writer = getWriter();
			Document doc = getIndexableFields(article);
			writer.updateDocument(new Term("id",String.valueOf(article.getArticleId())),doc);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	private Document getIndexableFields(Article article) throws Exception {
		Document doc = new Document();
		doc.add(new StringField("id", String.valueOf(article.getArticleId()), Field.Store.YES));
		doc.add(new TextField("name", article.getName(), Field.Store.YES));
		doc.add(new StringField("publishDate", DateUtil.formatDate(article.getPublishDate(), "yyyy-MM-dd HH:mm"), Field.Store.YES));
		doc.add(new TextField("content", article.getContentNoTag(), Field.Store.YES));
		doc.add(new StringField("points", String.valueOf(article.getPoints()), Field.Store.YES));
		doc.add(new StringField("click", String.valueOf(article.getClick()), Field.Store.YES));
		doc.add(new StringField("arcTypeName", article.getArcType().getArcTypeName(), Field.Store.YES));
		doc.add(new StringField("headPortrait", article.getUser().getHeadPortrait(), Field.Store.YES));
		doc.add(new StringField("nickname", article.getUser().getNickname(), Field.Store.YES));
		doc.add(new StringField("isVip", String.valueOf(article.getUser().isVip()), Field.Store.YES));
		doc.add(new StringField("vipGrade", String.valueOf(article.getUser().getVipGrade()), Field.Store.YES));
		doc.add(new StringField("isFree", String.valueOf(article.isFree()), Field.Store.YES));
		doc.add(new StringField("isHot", String.valueOf(article.isHot()), Field.Store.YES));
		return doc;
	}

	/**
	 * 删除资源索引
	 */
	public void deleteIndex(String id){
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		try {
			IndexWriter writer = getWriter();
			writer.deleteDocuments(new Term("id",id));
			writer.forceMergeDeletes();         //强制删除
			writer.commit();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}

	/**
	 * 查询资源无高亮列表
	 */
	public List<Article> searchNoHighLighter(String q) throws IOException, ParseException {
		q = q.replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5]","");
		dir = FSDirectory.open(Paths.get(lucenePath));
		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher is = new IndexSearcher(reader);
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		QueryParser parser = new QueryParser("name",analyzer);
		Query query = parser.parse(q);
		QueryParser parser2 = new QueryParser("content",analyzer);
		Query query2 = parser2.parse(q);
		BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
		booleanQuery.add(query, BooleanClause.Occur.SHOULD);
		booleanQuery.add(query2, BooleanClause.Occur.SHOULD);
		TopDocs hits = is.search(booleanQuery.build(),10);
		List<Article> articleList = new ArrayList<>();
		ScoreDoc[] scoreDocs = hits.scoreDocs;
		for(ScoreDoc scoreDoc:scoreDocs){
			Document doc = is.doc(scoreDoc.doc);
			Article article = new Article();
			article.setArticleId(Integer.parseInt(doc.get("id")));
			article.setName(doc.get("name"));
			article.setClick(Integer.parseInt(doc.get("click")));
			articleList.add(article);
		}
		return articleList;
	}

	/**
	 * 查询资源详细列表，带高亮信息
	 */
	public List<Article> search(String q) throws IOException, ParseException, InvalidTokenOffsetsException, java.text.ParseException {
		q = q.replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5]","");
		dir = FSDirectory.open(Paths.get(lucenePath));
		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher is = new IndexSearcher(reader);
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		QueryParser parser = new QueryParser("name",analyzer);
		Query query = parser.parse(q);
		QueryParser parser2 = new QueryParser("content",analyzer);
		Query query2 = parser2.parse(q);
		BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
		booleanQuery.add(query, BooleanClause.Occur.SHOULD);
		booleanQuery.add(query2, BooleanClause.Occur.SHOULD);
		TopDocs hits = is.search(booleanQuery.build(),100);
		List<Article> articleList = new ArrayList<>();
		QueryScorer scorer = new QueryScorer(query);
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
		SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");
		Highlighter highlighter = new Highlighter(simpleHTMLFormatter,scorer);
		highlighter.setTextFragmenter(fragmenter);
		ScoreDoc[] scoreDocs = hits.scoreDocs;
		for(ScoreDoc scoreDoc:scoreDocs){
			Document doc = is.doc(scoreDoc.doc);
			Article article = new Article();
			article.setArticleId(Integer.parseInt(doc.get("id")));
			String name = doc.get("name");
			if(name!=null){
				TokenStream tokenStream = analyzer.tokenStream("name",new StringReader(name));
				String hName = highlighter.getBestFragment(tokenStream,name);
				if(StringUtils.isBlank(hName)){
					article.setName(name);
				}else{
					article.setName(hName);
				}
			}
			article.setClick(Integer.parseInt(doc.get("click")));
			article.setPublishDate(DateUtil.formatString(doc.get("publishDate"),"yyyy-MM-dd HH:mm"));
			article.setPoints(Integer.parseInt(doc.get("points")));
			article.setFree(Boolean.valueOf(doc.get("isFree")));
			article.setHot(Boolean.valueOf(doc.get("isHot")));
			ArcType arcType = new ArcType();
			arcType.setArcTypeName(doc.get("arcTypeName"));
			article.setArcType(arcType);
			User user = new User();
			user.setHeadPortrait(doc.get("headPortrait"));
			user.setNickname(doc.get("nickname"));
			user.setVip(Boolean.valueOf(doc.get("isVip")));
			user.setVipGrade(Integer.parseInt(doc.get("vipGrade")));
			article.setUser(user);
			articleList.add(article);
		}
		return articleList;
	}
}

