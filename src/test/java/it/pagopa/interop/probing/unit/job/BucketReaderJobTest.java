package it.pagopa.interop.probing.unit.job;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.springframework.boot.test.context.SpringBootTest;

import com.amazonaws.services.sqs.AmazonSQS;

import it.pagopa.interop.probing.dto.EserviceDTO;
import it.pagopa.interop.probing.job.BucketReaderJob;
import it.pagopa.interop.probing.producer.ServicesSend;
import it.pagopa.interop.probing.service.BucketServiceImpl;

@SpringBootTest
 class BucketReaderJobTest {

	@InjectMocks
    private BucketReaderJob job;  
    @Mock
    BucketServiceImpl bucketService;
    @Mock
	ServicesSend producer;
    @Mock
    private JobExecutionContext ctx;
    @Mock
    AmazonSQS amazon;
    
    List<EserviceDTO> listProva;

    @BeforeEach
    void setup(){
		listProva = new ArrayList<>();
		EserviceDTO eServiceDTO = new EserviceDTO();
		eServiceDTO.setEserviceId(UUID.randomUUID().toString());
    	eServiceDTO.setVersionId(UUID.randomUUID().toString());
    	eServiceDTO.setName("Service Name");
    	eServiceDTO.setProducerName("Producer Name");
    	eServiceDTO.setState("ACTIVE");
    	eServiceDTO.setType("REST");
		String[] basePath = { "xxx.xxx/xxx", "yyy.yyy/xxx" };
		eServiceDTO.setBasePath(basePath);
		listProva.add(eServiceDTO);
    }
    
	@Test
	@DisplayName("BucketReaderJob is executed")
	 void testBucketReaderJob_whenGivenValidJobExecutionContext_thenJobIsExecuted() throws Exception {
		
		Mockito.when(bucketService.readObject()).thenReturn(listProva);
		JobDetail jobDetail = mock(JobDetail.class); 
		JobKey jobKey = new JobKey("myJob"); 
		Mockito.when(jobDetail.getKey()).thenReturn(jobKey); 
		Mockito.when(ctx.getJobDetail()).thenReturn(jobDetail);
		Mockito.doNothing().when(producer).sendMessage(Mockito.any(EserviceDTO.class), Mockito.any());
		job.execute(ctx);
		verify(bucketService, times(1)).readObject();
	}
}
