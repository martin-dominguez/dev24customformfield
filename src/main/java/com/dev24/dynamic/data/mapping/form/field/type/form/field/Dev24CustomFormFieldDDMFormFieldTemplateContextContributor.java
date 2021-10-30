package com.dev24.dynamic.data.mapping.form.field.type.form.field;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.WebKeys;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

@Component(
	immediate = true, 
	property = "ddm.form.field.type.name=dev24customformfield",
	service = {
		DDMFormFieldTemplateContextContributor.class,
		Dev24CustomFormFieldDDMFormFieldTemplateContextContributor.class
	}
)
public class Dev24CustomFormFieldDDMFormFieldTemplateContextContributor
		implements DDMFormFieldTemplateContextContributor {

	@Override
	public Map<String, Object> getParameters(DDMFormField ddmFormField,
			DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {
		return HashMapBuilder.<String, Object>put(
				"userDataValue", getValue(ddmFormField, ddmFormFieldRenderingContext)
				).build();
	}
	
	protected ThemeDisplay getThemeDisplay(DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {
		HttpServletRequest httpServletRequest =
				ddmFormFieldRenderingContext.getHttpServletRequest();
			
		ThemeDisplay themeDisplay =
				(ThemeDisplay)httpServletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);
		
		return themeDisplay;
	}
	
	protected String getValue(DDMFormField ddmFormField,
			DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {
		
		ThemeDisplay td = getThemeDisplay(ddmFormFieldRenderingContext);
		
		String userData = "";
		
		if (td.isSignedIn()) {
			
			User currentUser = td.getUser();
			try {
				JSONArray jsonArray = JSONFactoryUtil.createJSONArray(
						(String)ddmFormField.getProperty("userData")
						);
				Method m = User.class.getMethod(jsonArray.getString(0));
				userData = (String) m.invoke(currentUser);
			} catch (JSONException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		
		}
		
		return userData;
	}

}
