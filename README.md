# Overview

# Prequisites

# Detailed Steps
## Chapter 0: Init Workspace and module from template
First initiate your workspace with the last availabale version of Liferay CE. Ensure to have blaed up-to-date.

**Note:** While I was writing this the current version was 4.0.10.2021102.162.^

`$ blade init -v 7.4 <Your_Workspace_Name>`

Then go to your workspace and create new module using the form-field template
`$ blade create -t form-field  -p <Your_Packge_Name> -c <Your_Main_Class_Name> <Your_Field_Name>`

## Chapter 1: Remove Soy references and add React dependences
Go to your module main folder and first edit bnb.bnb and package.json and remove every Soy references and update your packages with `npm install`

Then edit `.babelrc` and add the React's preset: `@babel/preset-react`. And install the module as development dependence: `npm install @babel/preset-react --save-dev`

Now edit the Liferay NPM Bundler configuration in '.npmbundlerrc' and add the new way to name the presest for this bundler.
```
<       "preset": "liferay-npm-bundler-preset-liferay-dev",
---
>       "preset": "@liferay/npm-bundler-preset-liferay-dev",
```

Install this new module and remove the previous one. Note that this new preset has a dependece on `css-loader` moule, so you'll need to install it as well. Don't forget also update NPM Bundler to the latest version:
```
$ npm i @liferay/npm-bundler-preset-liferay-dev --save-dev
$ npm i css-loader --save-dev
$ npm remove liferay-npm-bundler-preset-liferay-dev
$ npm i liferay-npm-bundler@latest --save-dev
```
**Note:** versions and dependeces may vary depending on the time since I wrote this. Pay attention to error messages.

Go to resources folder (`src/main/resources/META-INF/resources/`) and remove soy files: `rm *.soy`, we don't need them anymore.

Build for very first time to see that everything has properly downloaded and you don't have extra dependeces.
`gw build`

## Chapter 2: Hello World!!
Let's create a very simple example of a Custom Form Field, by creating just a field which renders a text in our forms.

Let's do React! Go to the resouces folder (`src/main/resources/META-INF/resources/`) edit from scratch your render template (`<your-module-name>.es.js`

Bear in mind two important things:
1. Liferay can render only React components, so your React file must export a React Component.
2. There is a [ReactFieldBase](https://github.com/liferay/liferay-portal/blob/7.4.x/modules/apps/dynamic-data-mapping/dynamic-data-mapping-form-field-type/src/main/resources/META-INF/resources/FieldBase/ReactFieldBase.es.js) that you can use to properly render your Field and get the main props that are used in forms. Take a look to its definition to see availabale props and functions.

## Chapter 3: Basic custom properties
Let's see what are the basic porperties that we can configure in our module. We can see it in our main (and only) class `<YOUR_MODULE>DDMFormFieldType`
* **ddm.form.field.type.description** Your module description shown in fields list. 
* **ddm.form.field.type.display.order** The order in the fields list
* **ddm.form.field.type.group** Group for your module, AFAIK it doesn't apply anymore in 7.4
* **ddm.form.field.type.icon** Icon to show in the list
* **ddm.form.field.type.label** The main label shown in the fields list
* **ddm.form.field.type.name** The key that you are going to use to link everything

Of course, you can have it everything in different languages, so my recommendation is: use the language.properties files located in `src/main/resources/content`. See more info about them [here](https://help.liferay.com/hc/es/articles/360028746692-Localizing-Your-Application)

## Chapter 4: Configuring the Custom Form Field
By creating an interface extending `DefaultDDMFormFieldTypeSettings` and linking it with our module, we can create configuration pages by ussing DDM [Form Annotations](https://learn.liferay.com/dxp/latest/en/developing-applications/core-frameworks/configuration-framework/ddm-form-annotations.html) from the [Configuration Framework](https://learn.liferay.com/dxp/latest/en/developing-applications/core-frameworks/configuration-framework.html)

This is the basic configuration in custom form field to properly work:
```
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
								"label", "predefinedValue", "required", "tip"
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
								"dataType", "name", 
								"type", "repeatable"
							}
						)
					}
				)
			}
		)
	}
)
```

Now, you can add more fields, rules, rewrite exisiting fields or wathever you need but following [Form Annotations](https://learn.liferay.com/dxp/latest/en/developing-applications/core-frameworks/configuration-framework/ddm-form-annotations.html)' way.

Let me provide and example. This is a custom configuration field that you must add in one yours `@DDMFormLayoutColumn`:
```
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
```
Finally in your Custom Form Field main class `<Your_Field_Name>DDMFormFieldType` you'll need to Overwrite `getDDMFormFieldTypeSettings` to link your field with the settings created.

```
@Override
public Class<? extends DDMFormFieldTypeSettings>
    getDDMFormFieldTypeSettings() {

    return <Your_Field_Name>DDMFormFieldTypeSettings.class;
}
```
Now, you can see you have a **new prop** in your React form, that you can use to change your field's behaviour.

## Chapter 5: Make it persistent
As you might osbserve, the prop with your data is only populated when you change the config value, but when the form is saved and re-redendered the configuration value doesn't persist. To get this, we must inject this value in the Form's Context by using a Context Contributor: [`DDMFormFieldTemplateContextContributor`](https://github.com/liferay/liferay-portal/blob/7.4.x/modules/apps/dynamic-data-mapping/dynamic-data-mapping-api/src/main/java/com/liferay/dynamic/data/mapping/form/field/type/DDMFormFieldTemplateContextContributor.java). This interface has a single method named `getParameters` which gets the settings values, allows to call internal and external services and inject parameter in our Form's Context. 

So create a new class in your module extending this class and check that the Component Annotation points to the right place within the property `ddm.form.field.type.name` and use `getParameters` to add information to the Context.

## Chapter 6: A real world example
Let's create something useful: a field which autofields out the input box with logged user data (screen name, email, name, last name...)

1. Modify settings to select the user attribute that you want to use to autofill out the field.
2. Get the user data stored in the `ThemeDisplay` (you can use the `UserLocalServiceUtil` as well) in your Context Contributor.
3. Put the data in the Context to autofill the field in the Context Contributor as well.
4. Get also the data in the front-end by using [Liferay JavaScript APIs](https://help.liferay.com/hc/es/articles/360029005792-Liferay-JavaScript-APIs) to preview the changes before saving. Note that also the [Headless APIs](https://learn.liferay.com/dxp/latest/en/headless-delivery/consuming_apis.html) can be used.

## Chapter 7: What else?
* Custom Validations
* Actions between page changes

## Appendix: More examples:
* [Complete User Data Form Field]
* [Image Checkbox]
* [Slider]
*



