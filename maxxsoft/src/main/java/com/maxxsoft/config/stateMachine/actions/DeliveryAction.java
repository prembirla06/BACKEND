/*******************************************************
* Copyright (C) 2020, TecMaXX GmbH
* All Rights Reserved.
* 
* NOTICE: All information contained herein is, and remains
* the property of TecMaXX GmbH and its suppliers,
* if any. The intellectual and technical concepts contained
* herein are proprietary to TecMaXX GmbH
* and its suppliers and are protected by trade secret or copyright law.
* Dissemination of this information or reproduction of this material
* is strictly forbidden unless prior written permission is obtained
* from TecMaXX GmbH.
* 
* TecMaXX GmbH
* Auf der Suend 18, DE-91757 Treuchtlingen
*******************************************************/
package com.maxxsoft.config.stateMachine.actions;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import com.maxxsoft.common.model.OrderEvents;
import com.maxxsoft.common.model.OrderStates;
import com.maxxsoft.microServices.mailService.model.Mail;
import com.maxxsoft.microServices.mailService.service.EmailSenderService;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Component
public class DeliveryAction implements Action<OrderStates, OrderEvents> {

	@Autowired
	private EmailSenderService emailSenderService;

	@Override
	public void execute(StateContext<OrderStates, OrderEvents> context) {
		System.out.println("delivery action started................");
		System.out.println(context.getStateMachine().getId());

		Mail mail = new Mail();
		mail.setFrom("maxxsoft@tecmaxx.de");
		mail.setMailTo("ms@algoson.com");
		mail.setSubject("Lieferung ihrer Bestellung von moebel-guenstig24.de");
		mail.setTemplateName("deliveryTour");

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("name", "Neo Maximillion");
		model.put("deliveryDateTime", LocalDateTime.now());
		model.put("deliveryPhoneNumber", "121212121212");
		model.put("${customerName}", "Neo");
		mail.setProps(model);

		try {
			emailSenderService.sendEmail(mail);
		} catch (MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("delivery action was called!!!");
	}
}