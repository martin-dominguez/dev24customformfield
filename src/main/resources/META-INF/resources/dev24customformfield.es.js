import React from 'react';
import {FieldBase} from 'dynamic-data-mapping-form-field-type/FieldBase/ReactFieldBase.es';

/**
 * Dev24customformfield React Component
 */
const Dev24CustomFormField = ({sayHelloTo, ...otherProps}) => {
	console.log(sayHelloTo, otherProps);
	return (
		<FieldBase {...otherProps}>
			<h1>Hello {sayHelloTo}!</h1>
		</FieldBase>
	);
}

export default Dev24CustomFormField;
