# Overview
This module has been created for /dev/24 2021, a Liferay Community Event.

The main target of this session is letting know what are the keys when you want to leverage all the power of forms and create your own custom fields. No matter what, if it can be done with React you can use it in your forms.

Although actually the Forms and Docs teams are working hard to document this feature and provide examples with great snippets of code trying to shed some light about the different pieces that you can use.

# Prerequisites
## Node.js
[Node.js](https://nodejs.org/es/docs/) is a JavaScript runtime built on Chrome's V8 JavaScript engine, designed to build scalable network applications.
**Version used for this session**: v12.19.0

## npm
[npm](https://docs.npmjs.com/cli/v7/commands/npm) is the package manager for the Node JavaScript platform. It puts modules in place so that node can find them, and manages dependency conflicts intelligently. It is extremely configurable to support a variety of use cases. Most commonly, you use it to publish, discover, install, and develop node programs.

**Version used for this session**: 7.20.0

## Blade CLI
[Blade CLI](https://learn.liferay.com/dxp/latest/en/developing-applications/tooling/blade-cli/generating-projects-with-blade-cli.html) exists to create, build, and deploy Liferay projects in Liferay Workspaces. Once created, these projects can be imported into an IDE or worked on directly. Here you’ll learn the various ways in which you can create and manage Liferay projects.

**Version used for this session**:blade version 4.0.10.SNAPSHOT202110281623

## Liferay Portal
I guess if you didn't know what Liferay Portal is, you wouldn't be here :) 

**Version used for this session**: Liferay Portal 7.4 GA3

**Note (Oct 31th 2021)**: I tried to test this project with 7.4 GA4, but I found an error with Field deployment.

## Other software that you may know
Although these programs don't need to be installed directly, it will be installed automatically and it's worth it to know what they do.

### React
Well, the essence of this session. Our target is creating Form Field by using React, so I assume you know what React is. Anyway, [React or ReactJS](https://reactjs.org/) is a Javascript library for building user interfaces and the great thing with React is that it has been designed from the start for gradual adoption.

**Version used for this session**: Currently Liferay Portal uses React: 16.12.0

### Babel
[Babel](https://babeljs.io/) is a JavaScript compiler. Basically translates JS next-gen code to browsers-compatible JS.

We will use liferay-npm-bundler to compile our code through Babel.

**Version used for this session**: Last version of the [`liferay-npm-bundler-loader-babel-loader`](https://github.com/liferay/liferay-frontend-projects/tree/master/maintenance/projects/js-toolkit/packages/liferay-npm-bundler-loader-babel-loader) includes version 6.26.3

### liferay-npm-bundler
[`liferay-npm-bundler`](https://github.com/liferay/liferay-frontend-projects/tree/master/maintenance/projects/js-toolkit/packages/liferay-npm-bundler) is a tool tool to process a Liferay widget project to produce an OSGi bundle containing the needed npm dependencies so that it can be run when deployed to the Portal

**Version used for this session**: 2.27.0

# Detailed Steps
## Chapter 0: Init Workspace and module from template
First initiate your workspace with the last available version of Liferay CE. Ensure to have blaed up-to-date.

**Note:** While I was writing this the current version was 4.0.10.2021102.162.^

`$ blade init -v 7.4 <Your_Workspace_Name>`

Then go to your workspace and create new module using the form-field template
`$ blade create -t form-field  -p <Your_Packge_Name> -c <Your_Main_Class_Name> <Your_Field_Name>`

## Chapter 1: Remove Soy references and add React dependences
Go to your module main folder and first edit bnb.bnb and package.json and remove every Soy references and update your packages with `npm install`

Then edit `.babelrc` and add the React's preset: `@babel/preset-react`. And install the module as development dependence: `npm install @babel/preset-react --save-dev`

Now edit the Liferay NPM Bundler configuration in '.npmbundlerrc' and add the new way to name the preset for this bundler.
```
<       "preset": "liferay-npm-bundler-preset-liferay-dev",
---
>       "preset": "@liferay/npm-bundler-preset-liferay-dev",
```

Install this new module and remove the previous one. Note that this new preset has a dependence on the `css-loader` module, so you'll need to install it as well. Don't forget also update NPM Bundler to the latest version:
```
$ npm i @liferay/npm-bundler-preset-liferay-dev --save-dev
$ npm i css-loader --save-dev
$ npm remove liferay-npm-bundler-preset-liferay-dev
$ npm i liferay-npm-bundler@latest --save-dev
```
**Note:** versions and dependencies may vary depending on the time since I wrote this. Pay attention to error messages.

Go to resources folder (`src/main/resources/META-INF/resources/`) and remove soy files: `rm *.soy`, we don't need them anymore.

Build for the very first time to see that everything has been properly downloaded and you don't have extra dependencies.
`gw build`

## Chapter 2: Hello World!!
Let's create a very simple example of a Custom Form Field, by creating just a field which renders a text in our forms.

Let's do React! Go to the resources folder (`src/main/resources/META-INF/resources/`) edit from scratch your render template (`<your-module-name>.es.js`

Bear in mind two important things:
1. Liferay can render only React components, so your React file must export a React Component.
2. There is a [ReactFieldBase](https://github.com/liferay/liferay-portal/blob/7.4.x/modules/apps/dynamic-data-mapping/dynamic-data-mapping-form-field-type/src/main/resources/META-INF/resources/FieldBase/ReactFieldBase.es.js) that you can use to properly render your Field and get the main props that are used in forms. Take a look at its definition to see available props and functions.

## Chapter 3: Basic custom properties
Let's see what are the basic properties that we can configure in our module. We can see it in our main (and only) class `<YOUR_MODULE>DDMFormFieldType`
* **ddm.form.field.type.description** Your module description, shown in the fields list. 
* **ddm.form.field.type.display.order** The order in the fields list
* **ddm.form.field.type.group** Group for your module, AFAIK it doesn't apply anymore in 7.4
* **ddm.form.field.type.icon** Icon to show in the list
* **ddm.form.field.type.label** The main label shown in the fields list
* **ddm.form.field.type.name** The key that you are going to use to link everything

Of course, you can have everything in different languages, so my recommendation is: use the language.properties files located in `src/main/resources/content`. See more info about them [here](https://help.liferay.com/hc/es/articles/360028746692-Localizing-Your-Application)

## Chapter 4: Configuring the Custom Form Field
By creating an interface extending `DefaultDDMFormFieldTypeSettings` and linking it with our module, we can create configuration pages by using DDM [Form Annotations](https://learn.liferay.com/dxp/latest/en/developing-applications/core-frameworks/configuration-framework/ddm-form-annotations.html) from the [Configuration Framework](https://learn.liferay.com/dxp/latest/en/developing-applications/core-frameworks/configuration-framework.html)

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

Now, you can add more fields, rules, rewrite existing fields or whatever you need but following [Form Annotations](https://learn.liferay.com/dxp/latest/en/developing-applications/core-frameworks/configuration-framework/ddm-form-annotations.html)' way.

Let me provide an example. This is a custom configuration field that you must add in one yours `@DDMFormLayoutColumn`:
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
As you might observe, the prop with your data is only populated when you change the config value, but when the form is saved and re-redendered the configuration value doesn't persist. To get this, we must inject this value in the Form's Context by using a Context Contributor: [`DDMFormFieldTemplateContextContributor`](https://github.com/liferay/liferay-portal/blob/7.4.x/modules/apps/dynamic-data-mapping/dynamic-data-mapping-api/src/main/java/com/liferay/dynamic/data/mapping/form/field/type/DDMFormFieldTemplateContextContributor.java). This interface has a single method named `getParameters` which gets the settings values, allows us to call internal and external services and inject parameters in our Form's Context. 

So create a new class in your module extending this class and check that the Component Annotation points to the right place within the property `ddm.form.field.type.name` and use `getParameters` to add information to the Context.

## Chapter 6: A real world example
Let's create something useful: a field which autofields the input box with logged user data (screen name, email, name, last name...)

1. Modify settings to select the user attribute that you want to use to autofill out the field.
2. Get the user data stored in the `ThemeDisplay` (you can use the `UserLocalServiceUtil` as well) in your Context Contributor.
3. Put the data in the Context to autofill the field in the Context Contributor as well.
4. Get also the data in the front-end by using [Liferay JavaScript APIs](https://help.liferay.com/hc/es/articles/360029005792-Liferay-JavaScript-APIs) to preview the changes before saving. Note that also the [Headless APIs](https://learn.liferay.com/dxp/latest/en/headless-delivery/consuming_apis.html) can be used.

# Appendix

## Appendix I: More examples:
* [Complete User Data Form Field (7.3-master/7.4)](https://github.com/martin-dominguez/custom-liferay-ddm-form-fields/tree/master/modules/userdata-field)
* [Image Checkbox (7.3)](https://github.com/lfrsales/custom-liferay-ddm-form-fields/tree/master/modules/image-checkbox)
* [Character Limited Text (7.3)](https://github.com/lfrsales/custom-liferay-ddm-form-fields/tree/master/modules/character-limited-text)
* [Slider (7.3)](https://github.com/marcosapmf/slider)

## Appendix II: What else?
During these release of Liferay Portal 7.4 we had a couple of announcements from the Forms team new features
### Release 7.4 GA1
* Upload files as guest
* Custom Submit button label
* Change settings location

### Release 7.4 GA2
* Confirm Fields
* Actions between [page changes](https://github.com/martin-dominguez/dev24custompagechange/tree/master)

### Release 7.4 GA3
* Nothing new here

### Release 7.4 GA4
* Boolean fields
* Export/Import structures
* Validations for date fields
* Mask for numeric fields
* Addresses from GoogleMaps



