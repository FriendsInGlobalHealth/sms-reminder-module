package org.openmrs.module.smsreminder.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.smsreminder.SmsReminderUtils;
import org.openmrs.module.smsreminder.api.SmsReminderService;
import org.openmrs.module.smsreminder.modelo.NotificationFollowUpPatient;
import org.openmrs.module.smsreminder.modelo.NotificationPatient;

/**
 * Created by nelson.mahumane on 20-10-2015. Classe que organiza todos recursos
 * necessarios para o envio de sms filtrando as de a cordo com as suas categoria
 */
public class SmsReminderResource {

	public static List<NotificationPatient> getAllNotificationPatiens() {
		final AdministrationService administrationService = Context.getAdministrationService();
		Context.getPatientService();
		final SmsReminderService smsReminderService = SmsReminderUtils.getService();
		final GlobalProperty gpRemainDays = administrationService.getGlobalPropertyObject("smsreminder.remaindays");
		final String remainDays = gpRemainDays.getPropertyValue();
		final List<NotificationPatient> notificationPatientsAll = new ArrayList<NotificationPatient>();
		final String days[] = remainDays.split(",");
		int i = 0;

		while (i < days.length) {

			final List<NotificationPatient> notificationPatients = smsReminderService
					.getNotificationPatientByDiasRemanescente(Integer.valueOf(days[i]));
			if ((notificationPatients != null) && !notificationPatients.isEmpty()) {

				notificationPatientsAll.addAll(notificationPatients);
			}

			i++;
		}
		return notificationPatientsAll;
	}

	public static List<NotificationFollowUpPatient> getAllNotificationFolowUpPatient() {
		Context.getAdministrationService();

		final SmsReminderService smsReminderService = SmsReminderUtils.getService();

		final List<NotificationFollowUpPatient> notificationFollowUpPatients = new ArrayList<NotificationFollowUpPatient>();
		notificationFollowUpPatients.addAll(smsReminderService.searchFollowUpPatient());

		return notificationFollowUpPatients;

	}

	public static List<NotificationPatient> tmpGetAllNotificationPatiens() {
		List<NotificationPatient> notificationPatients = new ArrayList<NotificationPatient>();
		
		NotificationPatient p1 = new NotificationPatient();
		p1.setNid("123456789");
		p1.setTelemovel("846294045");
		p1.setNome("JP Boane");
		p1.setSexo("M");
		p1.setDiasRemanescente(15);
		p1.setIdentificador(123455667);
		p1.setTipoVisita(1);
		p1.setUltimaVisita(new Date());
		p1.setProximaVisita(new Date());
		p1.setInicioTarv(new Date());
		
		notificationPatients.add(p1);
		
		return notificationPatients;
	}
	
}