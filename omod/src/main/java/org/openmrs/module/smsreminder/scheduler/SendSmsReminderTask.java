package org.openmrs.module.smsreminder.scheduler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.smsreminder.modelo.NotificationPatient;
import org.openmrs.module.smsreminder.utils.DatasUtil;
import org.openmrs.module.smsreminder.utils.DateUtil;
import org.openmrs.module.smsreminder.utils.SmsProperties;
import org.openmrs.module.smsreminder.utils.SmsReminderHelper;
import org.openmrs.module.smsreminder.utils.SmsReminderResource;
import org.openmrs.module.smsreminder.utils.Validator;
import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * Created by nelson.mahumane on 20-10-2015.
 */
public class SendSmsReminderTask extends AbstractTask {
    private Log log = LogFactory.getLog(getClass());

    @SuppressWarnings("unchecked")
	@Override
    public void execute() {
        Context.openSession();
        System.out.println("Start sms reminder task... "); 
        try {
            AdministrationService administrationService = Context.getAdministrationService();
            
            List<NotificationPatient> notificationPatients = SmsReminderResource.getAllNotificationPatiens();
            
            GlobalProperty gpPort = administrationService.getGlobalPropertyObject("smsreminder.port");
            
            String port = gpPort.getPropertyValue();
            
            GlobalProperty gpMessage = administrationService.getGlobalPropertyObject("smsreminder.message");
            
            String message = gpMessage.getPropertyValue();
            
            GlobalProperty gpUs = administrationService.getGlobalPropertyObject("smsreminder.us");
            
            String us = gpUs.getPropertyValue();
            
            LocationService locationService = Context.getLocationService();
            
            	if (new SmsReminderHelper().isNodeInstalled()) { 
					System.out.println("node is installed..."); 
					
            		if (notificationPatients != null && !notificationPatients.isEmpty()) {
            			
            			System.out.println("Size do loop..." + notificationPatients.size());  
            			Iterator<NotificationPatient> it = notificationPatients.iterator();
            			
            			List<NotificationPatient> patientList = IteratorUtils.toList(it);
            			
            			for (int i = 0; i < patientList.size(); i++) {            			
            			/*while (it.hasNext()) {
            				NotificationPatient notificationPatient = it.next();*/
            				
            				String messagem = (patientList.get(i).getSexo().equals("M")) ?
	    						"O sr: " + patientList.get(i).getNome() + " " + message + " " + "no " + locationService.getLocation(Integer.valueOf(us)).getName() 
	    						+ " " + "no dia " + DatasUtil.formatarDataPt(patientList.get(i).getProximaVisita()) :
	    							"A sra: " + patientList.get(i).getNome() + " " + message + " " + "no " + locationService.getLocation(Integer.valueOf(us)).getName() 
	    							+ " " + "no dia " + DatasUtil.formatarDataPt(patientList.get(i).getProximaVisita());
	    						System.out.println("A mensagem a ser enviada " + messagem); 
	    						
	    						try {
	    							String nodeCall = SmsProperties.NODE + " " + SmsProperties.PATH_TO_NODE + " " + port + " " 
	    										+ Validator.cellNumberValidator(patientList.get(i).getTelemovel()) + " " + messagem + " " + DateUtil.parseDate(patientList.get(i).getProximaVisita()) + " " + 
	    										patientList.get(i).getDiasRemanescente() + " " + patientList.get(i).getIdentificador();
	    							System.out.println("node call: " + nodeCall); 
	    							
	    							/*Process process = Runtime.getRuntime().exec(SmsProperties.NODE + SmsProperties.PATH_TO_NODE + SmsProperties.SMSNODEJS + port + " " 
	    									+ Validator.cellNumberValidator(notificationPatient.getTelemovel()) + " " + "Testing" + " " + DateUtil.parseDate(notificationPatient.getProximaVisita()) + " " + 
	    									notificationPatient.getDiasRemanescente() + " " + notificationPatient.getIdentificador());*/
	    							
	    							List<String> command = new ArrayList<String>();
	    						    command.add(SmsProperties.NODE);
	    						    command.add(SmsProperties.PATH_TO_NODE);
	    						    command.add(port);
	    						    command.add(Validator.cellNumberValidator(patientList.get(i).getTelemovel()));
	    						    command.add(messagem);
	    						    command.add(DateUtil.parseDate(patientList.get(i).getProximaVisita()));
	    						    command.add(patientList.get(i).getDiasRemanescente().toString());
	    						    command.add(patientList.get(i).getIdentificador().toString());

	    						    ProcessBuilder builder = new ProcessBuilder(command);

	    						    final Process process = builder.start();
	    							
	    							long now = System.currentTimeMillis();
	    							long timeoutInMillis = 1000L * 10;
	    							long finish = now + timeoutInMillis;
	    							while (isAlive(process)) {
	    								Thread.sleep(10);
	    								if (System.currentTimeMillis() > finish) {
	    									process.destroy();
	    								}
	    							}
    								
	    						} catch (IOException e) {
	    							e.printStackTrace();
	    						} 
            			}
            		}
				}
            
        } catch (Throwable t) {
            log.error("Error while sending SMS ", t);
            throw new APIException(t);
        } finally {
            Context.closeSession();
        }
        log.info("End sms reminder task");
    }
    
    public static boolean isAlive(Process p) {
		try {
			p.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
	}
}