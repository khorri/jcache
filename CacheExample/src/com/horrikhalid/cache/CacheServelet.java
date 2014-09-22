package com.horrikhalid.cache;

import java.io.IOException;

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

/**
 * Servlet implementation class CacheServelet
 */
@WebServlet("/CacheServelet")
public class CacheServelet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Cache<String, String> cache;    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CacheServelet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	
	if(this.cache.get("hello-world")==null){
		this.cache.put("hello-world", "new hello world");
		response.getWriter().write("The Hello-world value is expired - ");
	}
		response.getWriter().write(this.cache.get("hello-world"));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		if(this.cache == null){
			CachingProvider provider = Caching.getCachingProvider();
			CacheManager manager = provider.getCacheManager();
			MutableConfiguration<String, String> configuration = new MutableConfiguration<String, String>()
					.setTypes(String.class, String.class)
					.setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.ONE_MINUTE))
					.setStatisticsEnabled(true);
			if(manager.getCache("cache")==null)
			 this.cache = manager.createCache("cache", configuration);
			cache.put("hello-world", "hello world");
		}
	}
	
	

}
