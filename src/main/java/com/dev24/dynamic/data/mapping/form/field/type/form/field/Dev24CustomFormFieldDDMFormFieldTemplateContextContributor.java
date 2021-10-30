package com.dev24.dynamic.data.mapping.form.field.type.form.field;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;

import java.util.Map;

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
				"sayHelloTo", getValue(ddmFormField, ddmFormFieldRenderingContext)
				).build();
	}
	
	protected String getValue(DDMFormField ddmFormField,
			DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {
		
		String sayHelloTo = "";
		try {
			JSONArray jsonArray = JSONFactoryUtil.createJSONArray(
					(String)ddmFormField.getProperty("sayHelloTo")
					);
			sayHelloTo = jsonArray.getString(0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return sayHelloTo;
	}

}