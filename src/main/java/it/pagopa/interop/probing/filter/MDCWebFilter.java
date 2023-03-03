package it.pagopa.interop.probing.filter;

import it.pagopa.interop.probing.util.constant.LoggingPlaceholders;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * WebFilter that puts in the MDC log map a unique identifier for incoming requests.
 */

@Component
public class MDCWebFilter extends OncePerRequestFilter {
	
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
			MDC.put(LoggingPlaceholders.TRACE_ID_PLACEHOLDER, "Root=" + UUID.randomUUID().toString().toLowerCase());
            filterChain.doFilter(request, response);
        }
		finally {
			MDC.remove(LoggingPlaceholders.TRACE_ID_PLACEHOLDER);
        }
    }
    
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return "/status".equals(request.getRequestURI());
	}
}