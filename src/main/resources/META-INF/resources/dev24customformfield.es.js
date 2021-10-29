import 'dynamic-data-mapping-form-field-type/FieldBase/FieldBase.es';
import './dev24customformfieldRegister.soy.js';
import templates from './dev24customformfield.soy.js';
import {Config} from 'metal-state';


/**
 * Dev24CustomFormField Component
 */
class Dev24CustomFormField extends Component {

	dispatchEvent(event, name, value) {
		this.emit(name, {
			fieldInstance: this,
			originalEvent: event,
			value
		});
	}

	_handleFieldChanged(event) {
		const {value} = event.target;

		this.setState(
			{
				value
			},
			() => this.dispatchEvent(event, 'fieldEdited', value)
		);
	}
}

Dev24CustomFormField.STATE = {

	name: Config.string().required(),

	predefinedValue: Config.oneOfType([Config.number(), Config.string()]),

	required: Config.bool().value(false),

	showLabel: Config.bool().value(true),

	spritemap: Config.string(),

	value: Config.string().value('')
}

// Register component
Soy.register(Dev24CustomFormField, templates);

