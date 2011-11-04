/**
 * SAHARA Labs Scheduling Server
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, University of Technology, Sydney
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of the University of Technology, Sydney nor the names
 *    of its contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Michael Diponio (mdiponio)
 * @date 21st October 2011
 */
 

function createClass()
{
	$("body").append(
		"<div id='perm-createclassdialog' title='Add a new class'><form>" +
			"<div>" +
				"<label for='newClassName'>Name:</label>" +
				"<input type='text' id='newClassName' class='validate[required]' />" +
			"</div>" +
			"<div>" +
				"<label for='newClassActive'>Active:</label>" +
				"<input type='checkbox' id='newClassActive' />" +
			"</div>" +
			"<div>" +
				"<label for='newClassQueuable'>Queuing:</label>" +
				"<input type='checkbox' id='newClassQueuable' />" +
			"</div>" +
			"<div>" +
				"<label for='newClassBookable'>Reservations:</label>" +
				"<input type='checkbox' id='newClassBookable' />" +
			"</div>" +
			"<div>" +
				"<label for='newClassPriority'>Priority:</label>" +
				"<input type='text' id='newClassPriority' value='1' class='validate[required,custom[integer],min[1]],max[255]' />" +
			"</div>" +
			"<div>" +
				"<label for='newClassTimeHorizon'>Time Horizon:</label>" +
				"<input type='text' id='newClassTimeHorizon' value='0' class='validate[required,custom[integer],min[0]]' />" +
			"</div>" +
		"</form></div>"
	);
	
	$("#perm-createclassdialog").dialog({
		resizable: false,
		modal: true,
		closeOnEscape: false,
		buttons: {
			'Create': saveCreateClass,
			'Cancel': function() {
				$(this).dialog('close');
			}
		},
		close: function() {
			$(this).dialog('destroy');
			$(".formError").remove();
			$(this).remove();
			
		},
		width: 400
	})
	.children("form").validationEngine();
	
	
}

function saveCreateClass() 
{
	if ($("#perm-createclassdialog form").validationEngine("validate"))
	{
		/* Tear down dialog. */
		$(this).parent().children(".ui-dialog-titlebar, .ui-dialog-buttonpane").hide();
		
		$(this).children("form").hide();
		$(this)
			.append(
				"<div class='perm-formspinner'>" +
					"<img src='/img/spinner.gif' alt='Loading...' />" +
					"<br /><br />" +
					"Adding..." +
				"</div>"
		);
		
		/* Save form. */
		$.post(
			"/userclasses/addClass",
			{
				name: $("#newClassName").val(),
				active: $("#newClassActive:checked").length == 1,
				queue: $("#newClassQueuable:checked").length == 1,
				bookable: $("#newClassBookable:checked").length == 1,
				priority: $("#newClassPriority").val(),
				horizon: $("#newClassTimeHorizon").val()
			},
			function(response) {
				if (typeof response != "object" || response.wasSuccessful)
				{
					window.location.reload();
				}
				else
				{
					/* Some error occurred. */
					$diag = $("#perm-createclassdialog");
					$diag.parent().children(".ui-dialog-titlebar, .ui-dialog-buttonpane").show();
					
					$diag.children(".perm-formspinner").remove();
					$diag.children("form").show()
						.prepend(
							"<div class='ui-state ui-state-error ui-corner-all'>" +
								"<span class='perm-uiicon ui-icon ui-icon-alert'></span>" +
								response.reason +
							"</div>"
						);
				}
			}
		);
	}
}