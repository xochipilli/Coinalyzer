package nu.dyn.caapi.coinalyzer.gui;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
public class CalendarView {
	private Date testDate;
	private Date trainDate;

	public void onDateSelect(SelectEvent event) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		facesContext.addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected",
						format.format(event.getObject())));
	}

	public void click() {
		RequestContext requestContext = RequestContext.getCurrentInstance();

		requestContext.update("form:display");
		requestContext.execute("PF('dlg').show()");
	}
	
	public void setTestDate(Date date) {
		this.testDate = date;
	}
	
	public void setTrainDate(Date date) {
		this.trainDate = date;
	}
	
	public Date getTrainDate() {
		return trainDate;
	}

	public Date getTestDate() {
		return testDate;
	}
	
	

}
