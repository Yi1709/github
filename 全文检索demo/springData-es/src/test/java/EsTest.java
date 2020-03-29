import com.lzw.es.pojo.Title;
import com.lzw.es.repositories.TitleRepositories;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class EsTest {

	@Autowired
	private TitleRepositories titleRepositories;

	@Autowired
	private ElasticsearchTemplate template;

	@Test
	public void createIndex() throws Exception {
		//创建索引，配置映射关系
		template.createIndex(Title.class);

	}

	/**
	 * 添加文档对象(更新操作是先删除后添加)
	 *
	 * @throws Exception
	 */
	@Test
	public void addDocument() throws Exception {
		for (int i = 1; i <= 10; i++) {
			Title title = new Title();
			title.setId(i);
			title.setTitle("Lucene" + i);
			title.setContent("Lucene是apache软件基金会4 jakarta项目组的一个子项目，是一个开放源代码的全文检索引擎工具包" + i);
			titleRepositories.save(title);
		}
	}

	@Test
	public void deleteDocument() throws Exception {
		titleRepositories.deleteById(2l);
	}

	@Test
	public void findAll() {
		Iterable<Title> all = titleRepositories.findAll();
		for (Title title : all) {
			System.out.println(title);
		}
	}

	@Test
	public void findById() {
		Optional<Title> optional = titleRepositories.findById((long) 3);
		Title title = optional.get();
		System.out.println(title);
	}
}
