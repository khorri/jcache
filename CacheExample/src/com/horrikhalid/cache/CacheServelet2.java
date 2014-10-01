package com.horrikhalid.cache;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.UUID;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.horrikhalid.dao.UserDao;
import com.horrikhalid.model.User;

/**
 * Servlet implementation class CacheServlet2
 */
@WebServlet("/CacheServelet2")
public class CacheServelet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cache<Integer, User> cache;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CacheServelet2() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		UserDao.initConnection();
		if(this.cache == null){
			CachingProvider provider = Caching.getCachingProvider();
			CacheManager manager = provider.getCacheManager();
			MutableConfiguration<Integer, User> configuration = new MutableConfiguration<Integer, User>()
					.setTypes(Integer.class, User.class)
					.setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.ONE_MINUTE))
					.setStatisticsEnabled(true);
			if(manager.getCache("cache")==null)
			 this.cache = manager.createCache("cache", configuration);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Write in DB & cache
		String action = (String)request.getAttribute("action");
		if(action != null && action.equals("w")){
			for(int i=0;i<100;i++){
				User user = new User();
				//user.setId(i);
				user.setFirstname(UUID.randomUUID().toString());
				user.setLastname(UUID.randomUUID().toString());
				user = UserDao.save(user);
				if(user != null)
					cache.put(user.getId(), user);
				
			}
		}
		// reading operation
		PrintWriter pw= response.getWriter();
		for(int i=0;i<100;i++){
			User user = cache.get(i);
			if(user==null){
				pw.write("load from database\n");
				user = UserDao.load(i);
				if(user != null){
					cache.put(user.getId(), user);
					pw.write(user.getFirstname()+"\n");
				}
			}else{
				pw.write("load from cache\n");
				pw.write(user.getFirstname()+"\n");
			}
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
