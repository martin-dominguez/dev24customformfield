import React from 'react';
import {FieldBase} from 'dynamic-data-mapping-form-field-type/FieldBase/ReactFieldBase.es';

/**
 * Dev24customformfield React Component
 */
const Dev24CustomFormField = (props) => {
	console.log(props);
	return (
		<FieldBase {...props}>
			<h1>Hello World!</h1>
		</FieldBase>
	);
}

export default Dev24CustomFormField;
