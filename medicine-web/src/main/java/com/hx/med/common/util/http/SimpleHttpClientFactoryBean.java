package com.hx.med.common.util.http;

import com.google.common.primitives.Ints;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultBackoffStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.DefaultServiceUnavailableRetryStrategy;
import org.apache.http.impl.client.FutureRequestExecutionService;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

import javax.net.ssl.HostnameVerifier;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by tengxianfei on 2016/11/2.
 *
 * @author tengxianfei
 * @since 1.6
 */
public final class SimpleHttpClientFactoryBean implements FactoryBean<SimpleHttpClient> {

  /**
   * Max connections per route.
   */
  public static final int MAX_CONNECTIONS_PRE_ROUTE = 50;

  /**
   * pool connections.
   */
  private static final int MAX_POOLED_CONNECTIONS = 100;

  /**
   * threads number.
   */
  private static final int DEFAULT_THREADS_NUMBER = 200;

  /**
   * timeout.
   */
  private static final int DEFAULT_TIMEOUT = 5000;

  /**
   * acceptable codes.
   */
  private static final int[] DEFAULT_ACCEPTABLE_CODES = new int[] {
    HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_NOT_MODIFIED, HttpURLConnection.HTTP_MOVED_TEMP,
    HttpURLConnection.HTTP_MOVED_PERM, HttpURLConnection.HTTP_ACCEPTED
  };

  /**
   * Queue size.
   */
  private static final int DEFAULT_QUEUE_SIZE  = (int) (DEFAULT_THREADS_NUMBER * 0.2);

  /**
   * Logger Object.
   */
  private final Logger logger = LoggerFactory.getLogger(SimpleHttpClientFactoryBean.class.getName());

  private int threadsNumber = DEFAULT_THREADS_NUMBER;

  private int queueSize = DEFAULT_QUEUE_SIZE;

  private int maxPooledConnections = MAX_POOLED_CONNECTIONS;

  private int maxConnectionsPerRoute = MAX_CONNECTIONS_PRE_ROUTE;

  private List<Integer> acceptables = Ints.asList(DEFAULT_ACCEPTABLE_CODES);

  private int connectionTimeout = DEFAULT_TIMEOUT;

  private int readTimeout = DEFAULT_TIMEOUT;

  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  private SSLConnectionSocketFactory sslConnectionSocketFactory = SSLConnectionSocketFactory.getSocketFactory();

  private HostnameVerifier hostnameVerifier = new DefaultHostnameVerifier();

  private CredentialsProvider credentialsProvider;

  private CookieStore cookieStore;

  private ConnectionReuseStrategy connectionReuseStrategy = new DefaultConnectionReuseStrategy();

  private ConnectionBackoffStrategy connectionBackoffStrategy = new DefaultBackoffStrategy();

  private ServiceUnavailableRetryStrategy serviceUnavailableRetryStrategy = new DefaultServiceUnavailableRetryStrategy();

  private Collection<? extends Header> defaultHeaders = Collections.emptyList();

  private AuthenticationStrategy authenticationStrategy = new ProxyAuthenticationStrategy();

  private boolean circularRedirectsAllowed = true;

  private boolean authenticationEnabled;

  private boolean redirectsEnabled = true;

  private ExecutorService executorService;

  @Override
  public SimpleHttpClient getObject() throws Exception {
    final CloseableHttpClient httpClient = buildHttpClient();
      final FutureRequestExecutionService futureRequestExecutionService = buildRequestExecutorService(httpClient);
    return new SimpleHttpClient(futureRequestExecutionService, httpClient, this.acceptables);
  }

  /**
   * Build HttpClient.
   * @return ClosebleHttpClient
   */
  private CloseableHttpClient buildHttpClient() {
    try {
      final ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
      final LayeredConnectionSocketFactory sslsf = this.sslConnectionSocketFactory;

      final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
        .register("http", plainsf)
        .register("https", sslsf)
        .build();

      final PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(registry);
      connMgr.setMaxTotal(this.maxPooledConnections);
      connMgr.setDefaultMaxPerRoute(this.maxConnectionsPerRoute);

      final HttpHost httpPost = new HttpHost(InetAddress.getLocalHost());
      final HttpRoute httpRoute = new HttpRoute(httpPost);
      connMgr.setMaxPerRoute(httpRoute, MAX_CONNECTIONS_PRE_ROUTE);

      final RequestConfig config = RequestConfig.custom()
        .setSocketTimeout(this.readTimeout)
        .setConnectionRequestTimeout(this.connectionTimeout)
        .setConnectTimeout(this.connectionTimeout)
        .setCircularRedirectsAllowed(this.circularRedirectsAllowed)
        .setRedirectsEnabled(this.redirectsEnabled)
        .setAuthenticationEnabled(this.authenticationEnabled)
        .build();

      final HttpClientBuilder httpClientBuilder = HttpClients.custom()
        .setConnectionManager(connMgr)
        .setDefaultRequestConfig(config)
        .setSSLSocketFactory(sslsf)
        .setSSLHostnameVerifier(this.hostnameVerifier)
        .setRedirectStrategy(this.redirectStrategy)
        .setDefaultCredentialsProvider(this.credentialsProvider)
        .setDefaultCookieStore(this.cookieStore)
        .setConnectionReuseStrategy(this.connectionReuseStrategy)
        .setConnectionBackoffStrategy(this.connectionBackoffStrategy)
        .setServiceUnavailableRetryStrategy(this.serviceUnavailableRetryStrategy)
        .setProxyAuthenticationStrategy(this.authenticationStrategy)
        .setDefaultHeaders(this.defaultHeaders)
        .useSystemProperties();

      return httpClientBuilder.build();
    } catch (final Exception e) {
      logger.error(e.getMessage(), e);
      throw new RuntimeException(e);
    }
  }

  /**
   * Build RequestExecutorService.
   *
   * @param httpClient HttpClient
   * @return FutureRequestExecutionService
   */
  private FutureRequestExecutionService buildRequestExecutorService(final CloseableHttpClient httpClient) {
    if (this.executorService == null) {
      this.executorService = new ThreadPoolExecutor(this.threadsNumber, this.threadsNumber, 0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingDeque<Runnable>(this.queueSize));
    }
    return new FutureRequestExecutionService(httpClient, this.executorService);
  }

  @Override
  public Class<?> getObjectType() {
    return SimpleHttpClient.class;
  }

  @Override
  public boolean isSingleton() {
    return false;
  }

  /**
   * get threadsNumber.
   *
   * @return int
   **/
  public int getThreadsNumber() {
    return threadsNumber;
  }

  /**
   * set threadsNumber.
   *
   * @param threadsNumber 线程数量
   */
  public void setThreadsNumber(final int threadsNumber) {
    this.threadsNumber = threadsNumber;
  }

  /**
   * get queueSize.
   *
   * @return int
   **/
  public int getQueueSize() {
    return queueSize;
  }

  /**
   * set queueSize.
   *
   * @param queueSize 队列大小
   */
  public void setQueueSize(final int queueSize) {
    this.queueSize = queueSize;
  }

  /**
   * get maxPooledConnections.
   *
   * @return int
   **/
  public int getMaxPooledConnections() {
    return maxPooledConnections;
  }

  /**
   * set maxPooledConnections.
   *
   * @param maxPooledConnections 线程池最大连接数
   */
  public void setMaxPooledConnections(final int maxPooledConnections) {
    this.maxPooledConnections = maxPooledConnections;
  }

  /**
   * get maxConnectionsPerRoute.
   *
   * @return int
   **/
  public int getMaxConnectionsPerRoute() {
    return maxConnectionsPerRoute;
  }

  /**
   * set maxConnectionsPerRoute.
   *
   * @param maxConnectionsPerRoute Max connection per route
   */
  public void setMaxConnectionsPerRoute(final int maxConnectionsPerRoute) {
    this.maxConnectionsPerRoute = maxConnectionsPerRoute;
  }

  /**
   * get acceptables.
   *
   * @return List
   **/
  public List<Integer> getAcceptables() {
    return acceptables;
  }

  /**
   * set acceptables.
   *
   * @param acceptables 可接受的状态码
   */
  public void setAcceptables(final List<Integer> acceptables) {
    this.acceptables = acceptables;
  }

  /**
   * get connectionTimeout.
   *
   * @return int
   **/
  public int getConnectionTimeout() {
    return connectionTimeout;
  }

  /**
   * set connectionTimeout.
   *
   * @param connectionTimeout 连接超时时间
   */
  public void setConnectionTimeout(final int connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  /**
   * get readTimeout.
   *
   * @return int
   **/
  public int getReadTimeout() {
    return readTimeout;
  }

  /**
   * set readTimeout.
   *
   * @param readTimeout 读超时时间
   */
  public void setReadTimeout(final int readTimeout) {
    this.readTimeout = readTimeout;
  }

  /**
   * get redirectStrategy.
   *
   * @return org.apache.http.client.RedirectStrategy
   **/
  public RedirectStrategy getRedirectStrategy() {
    return redirectStrategy;
  }

  /**
   * set redirectStrategy.
   *
   * @param redirectStrategy 重定向策略
   */
  public void setRedirectStrategy(final RedirectStrategy redirectStrategy) {
    this.redirectStrategy = redirectStrategy;
  }

  /**
   * get sslConnectionSocketFactory.
   *
   * @return org.apache.http.conn.ssl.SSLConnectionSocketFactory
   **/
  public SSLConnectionSocketFactory getSslConnectionSocketFactory() {
    return sslConnectionSocketFactory;
  }

  /**
   * set sslConnectionSocketFactory.
   *
   * @param sslConnectionSocketFactory SSLConnectionSocketFactory
   */
  public void setSslConnectionSocketFactory(final SSLConnectionSocketFactory sslConnectionSocketFactory) {
    this.sslConnectionSocketFactory = sslConnectionSocketFactory;
  }

  /**
   * get hostnameVerifier.
   *
   * @return javax.net.ssl.HostnameVerifier
   **/
  public HostnameVerifier getHostnameVerifier() {
    return hostnameVerifier;
  }

  /**
   * set hostnameVerifier.
   *
   * @param hostnameVerifier HostNameVerifier
   */
  public void setHostnameVerifier(final HostnameVerifier hostnameVerifier) {
    this.hostnameVerifier = hostnameVerifier;
  }

  /**
   * get credentialsProvider.
   *
   * @return org.apache.http.client.CredentialsProvider
   **/
  public CredentialsProvider getCredentialsProvider() {
    return credentialsProvider;
  }

  /**
   * set credentialsProvider.
   *
   * @param credentialsProvider CredentialsProvider
   */
  public void setCredentialsProvider(final CredentialsProvider credentialsProvider) {
    this.credentialsProvider = credentialsProvider;
  }

  /**
   * get cookieStore.
   *
   * @return org.apache.http.client.CookieStore
   **/
  public CookieStore getCookieStore() {
    return cookieStore;
  }

  /**
   * set cookieStore.
   *
   * @param cookieStore CookieStore
   */
  public void setCookieStore(final CookieStore cookieStore) {
    this.cookieStore = cookieStore;
  }

  /**
   * get connectionReuseStrategy.
   *
   * @return org.apache.http.ConnectionReuseStrategy
   **/
  public ConnectionReuseStrategy getConnectionReuseStrategy() {
    return connectionReuseStrategy;
  }

  /**
   * set connectionReuseStrategy.
   *
   * @param connectionReuseStrategy ConnectionReuseStrategy
   */
  public void setConnectionReuseStrategy(final ConnectionReuseStrategy connectionReuseStrategy) {
    this.connectionReuseStrategy = connectionReuseStrategy;
  }

  /**
   * get connectionBackoffStrategy.
   *
   * @return org.apache.http.client.ConnectionBackoffStrategy
   **/
  public ConnectionBackoffStrategy getConnectionBackoffStrategy() {
    return connectionBackoffStrategy;
  }

  /**
   * set connectionBackoffStrategy.
   *
   * @param connectionBackoffStrategy ConnectionBackoffStrategy
   */
  public void setConnectionBackoffStrategy(final ConnectionBackoffStrategy connectionBackoffStrategy) {
    this.connectionBackoffStrategy = connectionBackoffStrategy;
  }

  /**
   * get serviceUnavailableRetryStrategy.
   *
   * @return org.apache.http.client.ServiceUnavailableRetryStrategy
   **/
  public ServiceUnavailableRetryStrategy getServiceUnavailableRetryStrategy() {
    return serviceUnavailableRetryStrategy;
  }

  /**
   * set serviceUnavailableRetryStrategy.
   *
   * @param serviceUnavailableRetryStrategy ServiceUnavailableRetryStrategy
   */
  public void setServiceUnavailableRetryStrategy(final ServiceUnavailableRetryStrategy serviceUnavailableRetryStrategy) {
    this.serviceUnavailableRetryStrategy = serviceUnavailableRetryStrategy;
  }

  /**
   * get defaultHeaders.
   *
   * @return java.util.Collection
   **/
  public Collection<? extends Header> getDefaultHeaders() {
    return defaultHeaders;
  }

  /**
   * set defaultHeaders.
   *
   * @param defaultHeaders 默认header
   */
  public void setDefaultHeaders(final Collection<? extends Header> defaultHeaders) {
    this.defaultHeaders = defaultHeaders;
  }

  /**
   * get authenticationStrategy.
   *
   * @return org.apache.http.client.AuthenticationStrategy
   **/
  public AuthenticationStrategy getAuthenticationStrategy() {
    return authenticationStrategy;
  }

  /**
   * set authenticationStrategy.
   *
   * @param authenticationStrategy AuthenticationStrategy
   */
  public void setAuthenticationStrategy(final AuthenticationStrategy authenticationStrategy) {
    this.authenticationStrategy = authenticationStrategy;
  }

  /**
   * get circularRedirectsAllowed.
   *
   * @return boolean
   **/
  public boolean isCircularRedirectsAllowed() {
    return circularRedirectsAllowed;
  }

  /**
   * set circularRedirectsAllowed.
   *
   * @param circularRedirectsAllowed 是否允许重定向
   */
  public void setCircularRedirectsAllowed(final boolean circularRedirectsAllowed) {
    this.circularRedirectsAllowed = circularRedirectsAllowed;
  }

  /**
   * get authenticationEnabled.
   *
   * @return boolean
   **/
  public boolean isAuthenticationEnabled() {
    return authenticationEnabled;
  }

  /**
   * set authenticationEnabled.
   *
   * @param authenticationEnabled 是否需要验证
   */
  public void setAuthenticationEnabled(final boolean authenticationEnabled) {
    this.authenticationEnabled = authenticationEnabled;
  }

  /**
   * get redirectsEnabled.
   *
   * @return boolean
   **/
  public boolean isRedirectsEnabled() {
    return redirectsEnabled;
  }

  /**
   * set redirectsEnabled.
   *
   * @param redirectsEnabled 是否可重定向
   */
  public void setRedirectsEnabled(final boolean redirectsEnabled) {
    this.redirectsEnabled = redirectsEnabled;
  }

  /**
   * get executorService.
   *
   * @return java.util.concurrent.ExecutorService
   **/
  public ExecutorService getExecutorService() {
    return executorService;
  }

  /**
   * set executorService.
   *
   * @param executorService ExecutorService
   */
  public void setExecutorService(final ExecutorService executorService) {
    this.executorService = executorService;
  }
}
