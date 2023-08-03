ClassicEditor
				.create(document.querySelector('[data-editor="ckeditor"]'), {
					language: "ko",
					enterMode: 2,
					shiftEnterMode: 1,
					removePlugins: ['Indent'],
					autoParagraph: false,
				})
				.then(editor => {
					editor.model.document.on('change:data', () => {
						let editorData = editor.getData();
						document.getElementById('hiddenContent').value = editorData;
					});
				})
				.catch(error => {
					console.error(error);
				});

			function convertContent() {
				let editorData = CKEDITOR.instances.content.getData();
				document.getElementById('hiddenContent').value = editorData;
			}

			CKEDITOR.replace('testEditor', {
				height: 600
			});