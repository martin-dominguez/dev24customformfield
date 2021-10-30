package com.dev24.dynamic.data.mapping.form.field.type.form.field;

import com.liferay.dynamic.data.mapping.annotations.DDMForm;
import com.liferay.dynamic.data.mapping.annotations.DDMFormField;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayout;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutColumn;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutPage;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutRow;
import com.liferay.dynamic.data.mapping.form.field.type.DefaultDDMFormFieldTypeSettings;

@DDMForm
@DDMFormLayout(
	paginationMode = com.liferay.dynamic.data.mapping.model.DDMFormLayout.TABBED_MODE,
	value = {
		@DDMFormLayoutPage(
			title = "%basic",
			value = {
				@DDMFormLayoutRow(
					{
						@DDMFormLayoutColumn(
							size = 12,
							value = {
								"label", "sayHelloTo", "required", "tip"
							}
						)
					}
				)
			}
		),
		@DDMFormLayoutPage(
			title = "%properties",
			value = {
				@DDMFormLayoutRow(
					{
						@DDMFormLayoutColumn(
							size = 12,
							value = {
								"dataType", "name", "type", 
								"showLabel", "repeatable"
							}
						)
					}
				)
			}
		)
	}
)
public interface Dev24CustomFormFieldDDMFormFieldTypeSettings extends DefaultDDMFormFieldTypeSettings {
	@DDMFormField(
		label="%who-do-you-want-to-say-hello",
		optionLabels= {
				"%ray", "%alloy"
		},
		optionValues= {
				"Ray", "Alloy"
		},
		predefinedValue = "ray", 
		required = true,
		type="select"
	)
	public String sayHelloTo();
}
