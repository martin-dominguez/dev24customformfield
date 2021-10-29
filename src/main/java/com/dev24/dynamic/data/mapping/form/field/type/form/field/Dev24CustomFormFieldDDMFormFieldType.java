package com.dev24.dynamic.data.mapping.form.field.type.form.field;

import com.liferay.dynamic.data.mapping.form.field.type.BaseDDMFormFieldType;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldType;
import com.liferay.frontend.js.loader.modules.extender.npm.NPMResolver;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author mdominguez
 */
@Component(
	immediate = true,
	property = {
		"ddm.form.field.type.description=dev24customformfield-description",
		"ddm.form.field.type.display.order:Integer=13",
		"ddm.form.field.type.group=customized",
		"ddm.form.field.type.icon=emoji",
		"ddm.form.field.type.label=dev24customformfield-label",
		"ddm.form.field.type.name=dev24customformfield"
	},
	service = DDMFormFieldType.class
)
public class Dev24CustomFormFieldDDMFormFieldType extends BaseDDMFormFieldType {

	@Override
	public String getModuleName() {
		return _npmResolver.resolveModuleName(
			"dynamic-data-dev24customformfield-form-field/dev24customformfield.es");
	}

	@Override
	public String getName() {
		return "dev24customformfield";
	}

	@Override
	public boolean isCustomDDMFormFieldType() {
		return true;
	}

	@Reference
	private NPMResolver _npmResolver;

}