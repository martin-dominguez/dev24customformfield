import React, {useEffect} from 'react';
import {FieldBase} from 'dynamic-data-mapping-form-field-type/FieldBase/ReactFieldBase.es';
import {useSyncValue} from 'dynamic-data-mapping-form-field-type/hooks/useSyncValue.es';
import {ClayInput} from '@clayui/form';

/**
 * Dev24customformfield React Component
 */
const Dev24CustomFormField = ({disabled, name, onInput, value}) => (
	<ClayInput
		className="ddm-field-text"
		disabled={disabled}
		name={name}
		onInput={onInput}
		type="text"
		value={value}
	/>
);

const Main = props => {
	const {
		label,
		name,
		onChange,
		readOnly,
		userData,
		userDataValue,
		value,
		...otherProps
	} = props;

	const [currentValue, setCurrentValue] = useSyncValue(value ? value : userDataValue);

	useEffect(() => {
		if (Array.isArray(userData)) {
			
			let method = '';
			switch (userData[0]) {
				case 'getFullName':
					method = 'getUserName'
					break;
				case 'getEmailAddress':
					method = 'getUserEmailAddress'
					break;
			}

			setCurrentValue(Liferay.ThemeDisplay[method]());
		}
	}, [userData]);

	return (
		<FieldBase
			label={label}
			name={name}
			predefinedValue={userDataValue}
			{...otherProps}
		>
			<Dev24CustomFormField
				disabled = {readOnly}
				name = {name}
				onInput = {onChange}
				value = {currentValue}
			/>
		</FieldBase>
	);
}

export default Main;
