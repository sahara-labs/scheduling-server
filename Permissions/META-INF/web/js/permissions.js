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

function filterClass() 
{
	var search = $(this).val();
	
	if (search.length == 0)
	{
		/* Reshow all the hidden permissions. */
		$("#userclasslist li").show();
		return;
	}
	
	$("#userclasslist > li").each(function() {
		var id = $(this).attr("id").split("_").join(" ");

		if (id.indexOf(search) == -1)
		{
			if ($(this).css("display") != "none") $(this).hide();
		}
		else 
		{
			if ($(this).css("display") == "none") $(this).show();
		}
	});
}

function createClass()
{
	$("body").append(
		"<div id='perm-createclassdialog' title='Add a new class' class='saharaform'><form>" +
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
	
	$("#perm-createclassdialog input")
		.focusin(formFocusIn)
		.focusout(formFocusOut);
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
			"/permissions/addClass",
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

var userClassMode = [];
function editClass() 
{
	var $li = $(this).parents(".perm-userclass"), id = $li.attr("id");
	
	if (!userClassMode[id])
	{
		userClassMode[id] = true;
	
		$li.find("input").removeAttr("disabled");
		$li.find(".perm-editclass").empty().append(
			"<img alt='Save' src='/img/perm-save.png' />" +
			"<br />" +
			"Save"
		);
		
		$li.find(".perm-deleteclass").empty().append(
				"<img alt='Cancel' src='/img/perm-cancel.png' />" +
				"<br />" +
				"Cancel"
		);
	}
	else
	{
		$.post(
				"/permissions/updateClass",
				{
					name: id,
					active: $li.find(".isActive:checked").length == 1,
					queue: $li.find(".isQueuable:checked").length == 1,
					bookable: $li.find(".isBookable:checked").length == 1,
					priority: $li.find(".getPriority").val(),
					horizon: $li.find(".getTimeHorizon").val()
				},
				function(response) {
					if (typeof response != "object") 
					{
						window.location.reload();
					}
					else
					{
						userClassMode[id] = false;
						$li.find("input").attr("disabled", "disabled");
						$li.find(".perm-editclass").empty().append(
							"<img alt='Edit' src='/img/perm-edit.png' />" +
							"<br />" +
							"Edit"
						);
						
						$li.find(".perm-deleteclass").empty().append(
								"<img alt='Delete' src='/img/perm-delete.png' />" +
								"<br />" +
								"Delete"
						);
						
						if ($li.find(".isActive:checked").length == 1)
						{
							$li.children("a").removeClass("inactiveclass").addClass("activeclass");
						}
						else
						{
							$li.children("a").removeClass("activeclass").addClass("inactiveclass");
						}
					}
				}
			);
	}
}	

function deleteClass()
{
	var $li = $(this).parents(".perm-userclass"), id = $li.attr("id");
	
	if (!userClassMode[id])
	{
		$("body").append(
				"<div id='perm-deletedialog' title='Delete user class'>" +
					"<div class='ui-priority-primary'>" +
						"Are you sure you want to delete the class '" + id.split("_").join(" ") + "'?" +
					"</div>" +
					"<div class='ui-priority-secondary'>" +
						"<span class='ui-icon ui-icon-alert'></span>" +
						"This will delete the user class as well as all the permissions in the user class and " +
						"associations with users." +
					"</div>" +
				"</div>"
			);
			
			$("#perm-deletedialog").dialog({
				resizable: false,
				modal: true,
				closeOnEscape: false,
				buttons: {
					'Delete': function() {
						$.post(
							"/permissions/deleteClass",
							{ name: id },
							function (resp) {
								if (typeof resp != "object")
								{
									window.location.reload();
								}
								else
								{
									if (resp.wasSuccessful)
									{
										$li.remove();
										$("#perm-deletedialog").dialog("close");
									}
									else
									{
										$("#perm-deletedialog").prepend(
											"<div class='ui-state-error ui-corner-all' style='padding:5px;margin-bottom:10px'>" +
												"<span class='ui-icon ui-icon-alert'></span> Unable to delete class: " +
												resp.reason + 
											"</div>"
										);
									}
								}
							}
						);	
					},
					'Cancel': function() {
						$(this).dialog('close');
					}
				},
				close: function() {
					$(this).dialog('destroy');
					$(this).remove();
					
				},
				width: 400
			});
	}
	else
	{
		userClassMode[id] = false;
		$li.find("input").attr("disabled", "disabled");
		$li.find(".perm-editclass").empty().append(
			"<img alt='Edit' src='/img/perm-edit.png' />" +
			"<br />" +
			"Edit"
		);
		
		$li.find(".perm-deleteclass").empty().append(
				"<img alt='Delete' src='/img/perm-delete.png' />" +
				"<br />" +
				"Delete"
		);
	}
}

function addPermission() 
{
	drawPermissionDialog("Add Permission", $(this).parents("li").attr("id"));
}

function loadPermission() 
{
	var pid = $(this).attr('id').substr("perm-".length);
	
	drawPermissionDialog("Edit Permission: " + pid, $(this).parents("li").attr("id"));
	
	$("#permissiondialog form").append(
		"<input type='hidden' id='permissionId' value='" + pid + "' />"
	);
	
	$.post(
		"/permissions/getPermission",
		{ id: pid },
		function (resp) {
			var i;
			
			loadResources(resp.Type, resp.Resource);
			for (i in resp)
			{
				if (i == "UseActivityDectection")
				{
					if (resp[i])
					{
						$("#permissionUseActivityDetection").attr("checked", "checked");
					}
					else
					{
						$("#permissionUseActivityDetection").removeAttr("checked");
					}
				}
				else if (i == "Resource")
				{
					/* Do nothing. */
				}
				else
				{
					$("#permission" + i).val(resp[i]);
				}
			}
		}
	);
	
	$("#permissiondialog").dialog("option", "buttons", {
		'Save': savePermission,
		'Delete': deletePermission,
		'Close': function() { $(this).dialog("close"); }
	});
}

function drawPermissionDialog(title, userClass)
{
	var html =
		"<div id='permissiondialog' title='"+ title+ "'>" +
			"<form class='saharaform'>" +
							
				"<div id='permissiondialog-left' class='perm-col'>" + 

					"<div class='perm-header-break'><h3>Presentation</h3></div>" + 
					"<div>" +
						"<label for='permissionDisplayName'>Display name:</label>" +
						"<input type='text' id='permissionDisplayName' />" +
					"</div>" +
				
					/* Timeperiod. */
					"<div class='perm-header-break'><h3>Time Period</h3></div>" + 
					"<div class='timeline'>" +
						"<label for='permissionStartTime'>Start:</label>" +
						"<input type='text' id='permissionStartTime' class='periodcal validate[required]' />" +
						"<a id='startcalopen' class='perm-button calopen ui-corner-all'>" +
							"<img src='/img/daypicker.png' alt='Open' />" +
						"</a>" +
					"</div>" + 
					"<div class='timeline'>" +
						"<label for='permissionExpiryTime'>Expiry:</label>" +
						"<input type='text' id='permissionExpiryTime' class='periodcal validate[required]' />" +
						"<a id='expirycalopen' class='perm-button calopen ui-corner-all'>" +
							"<img src='/img/daypicker.png' alt='Open' />" +
						"</a>" +
					"</div>" + 
					
					/* Resource. */
					"<div class='perm-header-break'><h3>Resources</h3></div>" +
					"<div>" + 
						"<label for='permissionType'>Resource type:</label>" +
						"<select id='permissionType' class=;'validate[required]'>" +
							"<option value=''>&nbsp;</option>" +
							"<option value='Rig Type'>Rig Type</option>" +
							"<option value='Rig'>Rig</option>" +
							"<option value='Capabilities'>Capabilities</option>" +
						"</select>" +
					"</div>" +
					"<div>" + 
						"<label for='permissionResource'>Resource:</label>" +
						"<select id='permissionResource' class=;'validate[required]'>" +
							"<option value=''>&nbsp;</option>" +
						"</select>" +
					"</div>" +
					
				"</div>" +
				"<div id='permissiondialog-right' class='perm-col'>" + 
				
					/* Reservation. */
					"<div class='perm-header-break'><h3>Reservations</h3></div>" + 
					"<div>" +
						"<label for='permissionMaximumBookings'>Maximum concurrent reservations:</label>" +
						"<input type='text' id='permissionMaximumBookings' value='3' class='validate[required,custom[integer],min[0]]' />" +
					"</div>" +
				
					/* Session timing. */
					"<div class='perm-header-break'><h3>Session Timings</h3></div>" + 
					"<div>" +
						"<label for='permissionSessionDuration'>Session duration:</label>" +
						"<input type='text' id='permissionSessionDuration' value='1800' class='validate[required,custom[integer],min[0]]' />" +
					"</div>" +
					"<div>" +
						"<label for='permissionAllowedExtensions'>Allowed extensions:</label>" +
						"<input type='text' id='permissionAllowedExtensions' value='2' class='validate[required,custom[integer],min[0]]' />" +
					"</div>" +
					"<div>" +
						"<label for='permissionExtensionDuration'>Extension duration:</label>" +
						"<input type='text' id='permissionExtensionDuration' value='900' class='validate[required,custom[integer],min[0]]' />" +
					"</div>" +
					
					
					/* Timeouts. */
					"<div class='perm-header-break'><h3>Timeouts</h3></div>" + 
					"<div>" +
						"<label for='permissionQueueActivityTimeout'>Queue timeout:</label>" +
						"<input type='text' id='permissionQueueActivityTimeout' value='180' class='validate[required,custom[integer],min[0]]' />" +
					"</div>" +
					"<div>" +
						"<label for='permissionUseActivityDetection'>Use no activity timeout:</label>" +
						"<input type='checkbox' id='permissionUseActivityDetection' checked='checked' />" +
					"</div>" +
					"<div>" +
						"<label for='permissionSessionDetectionTimeout'>No Activity timeout:</label>" +
						"<input type='text' id='permissionSessionDetectionTimeout' value='600' class='validate[required,custom[integer],min[0]]' />" +
					"</div>" +
					
				"</div>" +
				"<div style='clear:both'></div>" +
				
				"<input type='hidden' id='permissionClass' value='" + userClass + "' />" +
				
			"</form>" + 
		"</div>";
	
	$("body").append(html);

	$("#permissiondialog").dialog({
		modal: true,
		width: 715,
		resizable: false,
		buttons: {
			'Save': savePermission,
			'Close': function() { $(this).dialog("close"); }
		},
		close: function() {
			$(this).children("form").validationEngine("hideAll");
			$(this).dialog("destroy").remove();
		}
		
	})
	.children("form").validationEngine();
	
	$("#permissiondialog input, #permissiondialog select")
		.focusin(formFocusIn)
		.focusout(formFocusOut);
	
	$(".periodcal").datetimepicker({
		dateFormat: "dd/mm/yy",
		showOn: "focus"
	});
	
	$("#startcalopen, #expirycalopen").click(function() {
		$(this).datetimepicker("show");
	});	
	
	$("#permissionType").change(function(val) {
		loadResources($(this).val());
	});
}

function savePermission() {
	
	if (!$("#permissiondialog form").validationEngine("validate"))
	{
		/* Failed validation. */
		return;
	}
	
	var params = { }, isUpdate = false;
	
	$('#permissiondialog input:not([type="checkbox"]), #permissiondialog select').each(function(i, e) {
		params[$(e).attr("id").substr(10)] = $(e).val();
	});
	params['UseActivityDectection'] = $("#permissionUseActivityDetection:checked").length === 1;
	
	if ($("#permissionId").length == 0)
	{
		/* Adding new permission. */
		target = "addPermission";
	}
	else
	{
		/* Saving existing permission. */
		target = "savePermission";
		isUpdate = true;
	}
	
	$.post(
		"/permissions/" + target,
		params,
		function(resp) {
			if (typeof resp != "object")
			{
				window.location.reload();
				return;
			}
			
			var name = $("#permissionDisplayName").val();
			if (resp.success && isUpdate)
			{
				$("#perm-" + $("#permissionId").val()).empty().append(
						 (name == "" ? $("#permissionType").val() + ": " +
									$("#permissionResource").val().split(" ").join(" ") : name)
				);
			}
			else if (resp.success)
			{
				var clazz = $("#permissionClass").val(),
						permList = $("#" + clazz + " .perm-permissionlist"), html = "";
				
				if (permList.length == 0)
				{
					/* Add permission list. */
					$("#" + clazz + " .perm-userclasspermissions").empty().append(
						"<ul class='perm-permissionlist'></ul>"
					);
					
					permList = $("#" + clazz + " .perm-permissionlist");
				}
			
				permList.append(
						"<li id='perm-" + resp.id + "'>" + (name == "" ? $("#permissionType").val() + ": " +
						$("#permissionResource").val().split("_").join(" ") : name) + "</li>");
				$("#perm-" + resp.id).click(loadPermission);
			}
			
			$("#permissiondialog").dialog("close");
		}
	);
}

function deletePermission()
{
	$("#permissiondialog").prepend(
		"<div id='permissionoverlay'></div>" +
		"<div id='permissionconfirm'>" +
			"<div>Are you sure you want to delete this permission?</div>" +
			"<div class='permissionconfirmbuttons'>" +
				"<button id='permissionconfirmcancel'>Cancel</button>" +
				"<button id='permissionconfirmdelete'>Delete</button>" +
			"</div>" +
		"</div>"
	);
	
	$("#permissionconfirmdelete, #permissionconfirmcancel").button();
	
	$("#permissionconfirmcancel").click(function() {
		$("#permissionoverlay, #permissionconfirm").remove();
	});
	
	$("#permissionconfirmdelete").click(function() {
		$.post(
			"/permissions/deletePermission",
			{ "pid": $("#permissionId").val() },
			function(resp) {
				if (resp.success)
				{
					var $li = $("#perm-" + $("#permissionId").val()), $ul = $li.parent();
					$li.remove();
					
					if ($ul.children().length == 0)
					{
						var $dv = $ul.parent();
						$ul.remove();
						
						$dv.append(
							'<div class="ui-state ui-state-highlight ui-corner-all">' +
    							'<span class="ui-icon ui-icon-info"></span>' +
    							'No permissions.' +
    						'</div>'
						);
					}
					
					$("#permissiondialog").dialog("close");
				}
				else
				{
					$("#permissionconfirm").prepend(
						"<div class='ui-state ui-state-error'>" +
							"<span class='ui-icon ui-icon-alert'></span>" + 
							"Failed to delete permission: " + resp.reason +
						"</div>"
					);
				}
			}
		);
	});
}

function loadResources(type, valset)
{
	var mtype = type.split(' ').join('').toUpperCase();
	
	$.post(
		"/permissions/loadResources",
		{ "type": mtype },
		function (ret) {
			var html = ""; 
			
			for (i in ret)
			{
				html += "<option value='" + ret[i] + "'>" + ret[i] + "</option>";
			}
			
			$("#permissionResource").empty().append(html)
				.prev().empty().append(type + ":");
			
			if (valset)
			{
				$("#permissionResource").val(valset);
			}
		}
	);
}

function addUserToClass()
{
	var $li = $(this).parents(".perm-userclass"), id = $li.attr("id");

	$("body").append(
			"<div id='perm-adduserdialog' title='Add User to Group: " + id.split("_").join(" ") + "'>" +
				"<div class='perm-autocomplete saharaform'>" +
					"<label for='perm-acinput'>Search: </label>" +
					"<input id='perm-acinput' type='text' />" +
				"</div>" + 
				
				"<div id='perm-acuserslist'>" +
				"</div>" + 
				
				"<div id='perm-selectedusers'>" +
					"<p>Users to add:</p>" +
					"<ul> </ul>" +
				"</div>" +
				
				"<div style='clear:both'> </div>" +
			"</div>"
	);

	$("#perm-adduserdialog").dialog({
		resizable: false,
		modal: true,
		closeOnEscape: true,
		width: 600,
		buttons: {
			'Add': function() {
				var addUsers = [];
				$("#perm-selectedusers input").each(function() {
					var id = $(this).attr('id');
					for (i in idsList)
					{
						if (idsList[i] == id)
						{
							addUsers.push(i);
							break;
						}
					}
				});
				
				if (addUsers.length > 0)
				{
					$.post(
						"/users/addUsersToClass",
						{
							users: addUsers.toString(),
							name: id
						},
						function(resp) {
							if (typeof resp != "object") window.location.reload();
							else ($("#perm-selectedusers ul").empty());
						}
					);
				}
			},
			'Close': function() { $(this).dialog("close") }
		},
		close: function() {
			$(this).dialog('destroy');
			$(this).remove();
		},
	});
	
	var lastSearch = "", idsList = [];
	
	$("#perm-acinput").keyup(function () {
		var search = $(this).val();
		
		if (search.length == lastSearch.length) return;
		lastSearch = search;
		
		if (search.length == 0) 
		{
			$("#perm-acuserslist").empty();
			return;
		}
		
		$.post(
			"/users/list",
			{ 
				search: search,
				notIn: id,
				max: 20
			},
			function (resp)
			{
				if (typeof resp != "object") 
				{
					window.location.reload();
					return;
				}

				var i = -1, html = "", len = Math.ceil(resp.length / 3), u;
				for (i in resp)
				{
					if (i > 0 && i % len == 0) html += "</ul></div>";
					if (i % len == 0) html += "<div class='perm-userscol'><ul>";
					
					u = resp[i];
					idsList[u.name] = "user-" + u.name.replace(/\.|\s|,/, "");
					html +=
						"<li>" +
							"<input type='checkbox' id='" + idsList[u.name] + "' />" +
							"<label for='" + idsList[u.name] + "'>" + u.display + "</label>" + 
						"</li>";
				}
				if (i != -1) html += "</ul></div>";

				$("#perm-acuserslist").empty().append(html);
				
				$("#perm-acuserslist input").change(function() {
					
					if ($(this).is(":checked"))
					{
						$(this).parent().detach().appendTo("#perm-selectedusers ul")
					}
					else
					{
						$(this).parent().remove();
					}
				});
			}
		);
		
	})
	.focusin(formFocusIn)
	.focusout(formFocusOut);
}


function deleteUserInClass()
{
	var usersList = [], idsList = [], oldSearch = "";
	
	var $li = $(this).parents(".perm-userclass"), id = $li.attr("id");
	
	/* Clear any old invocations. */
	usersList = [];
	idsList = [];
	oldSearch = "";

	$("body").append(
			"<div id='perm-deleteusersdialog' title='Remove from Group: " + id.split("_").join(" ") + "'>" +
				"<div id='perm-usersearch' class='saharaform'>" +
					"<label for='perm-usersearchinput'>Search: </label>" +
					"<input id='perm-usersearchinput' />" +
				"</div>" +
				"<div id='perm-userslist'> </div>" +
				"<div style='clear:both'> </div>" +
			"</div>"
	);

	$("#perm-deleteusersdialog").dialog({
		resizable: false,
		modal: true,
		closeOnEscape: true,
		width: 600,
		buttons: {
			'Remove': function() {
				var delUsers = [];
				
				$("#perm-deleteusersdialog input:checked").each(function() {
					var id = $(this).attr('id');
					for (i in idsList)
					{
						if (idsList[i] == id)
						{
							delUsers.push(i);
							break;
						}
					}
				});
				
				if (delUsers.length > 0)
				{
					$.post(
						"/users/deleteUsersInClass",
						{
							users: delUsers.toString(),
							name: id
						},
						function(resp) {
							if (typeof resp != "object") window.location.reload();
							else for (i in delUsers) $("#" + idsList[delUsers[i]]).parent().remove();
						}
					);
				}
			},
			'Close': function() {
				$(this).dialog("close");
			}
		},
		close: function() {
			$(this).dialog('destroy');
			$(this).remove();
		},
	});
			
	$.post(
		"/users/list",
		{ "in": id },
		function (resp) {
			if (typeof resp != "object")
			{
				window.location.reload();
				return;
			}
			
			resp.sort(function(a, b) {
				return a.display == b.display ? 0 : (a.display > b.display ? 1 : -1);
			});
			usersList = resp;
			
			var i = -1, html = "", len = Math.ceil(resp.length / 4), u;
			for (i in resp)
			{
				if (i > 0 && i % len == 0) html += "</ul></div>";
				if (i % len == 0) html += "<div class='perm-userscol'><ul>";
				
				u = resp[i];
				idsList[u.name] = "user-" + u.name.replace(/\.|\s|,/, "");
				html +=
					"<li>" +
						"<input type='checkbox' id='" + idsList[u.name] + "' />" +
						"<label for='" + idsList[u.name] + "'>" + u.display + "</label>" + 
					"</li>";
			}
			if (i != -1) html += "</ul></div>";
			
			$("#perm-userslist").append(html);
		}
	);
	
	$("#perm-usersearchinput").keyup(function() {
		var i, search = $(this).val(), nid;
		
		/* This accounts for key presses that do not add to the search. */
		if (search.length == oldSearch.length) return;
		
		oldSearch = search;
		
		if (search.length == 0)
		{
			$("#perm-deleteusersdialog li").css("display", "block");
			return;
		}
		
		for (i in usersList)
		{
			nid = "#" + idsList[usersList[i].name];
			if (usersList[i].display.indexOf(search) == -1)
			{
				$(nid).parent().css("display", "none");
			}
			else
			{
				$(nid).parent().css("display", "block");
			}
		}
	})
	.focusin(formFocusIn)
	.focusout(formFocusOut);
}

function deleteAllUsersInClass()
{
	var $li = $(this).parents(".perm-userclass"), id = $li.attr("id");

	$("body").append(
			"<div id='perm-removeallusersdialog' title='Remove all user associations'>" +
				"<div class='ui-priority-primary'>" +
					"Are you sure you want to delete all the user associations in the class " +
					"'" + id.split("_").join(" ") + "'?" +
				"</div>" +
				"<div class='ui-priority-secondary'>" +
					"<span class='ui-icon ui-icon-alert'></span>" +
					"Only the associations with the users will be deleted. The associated users will not be deleted " +
					"by this operation." +				
				"</div>" +
			"</div>"
	);

	$("#perm-removeallusersdialog").dialog({
		resizable: false,
		modal: true,
		closeOnEscape: true,
		width: 400,
		buttons: {
			'Remove': function() {
				$.post(
						"/permissions/deleteAllUsersInClass",
						{ name: id },
						function (resp) {
							if (typeof resp != "object")
							{
								window.location.reload();
							}
							
							$("#perm-removeallusersdialog").dialog("close");
						}
				);	
			},
			'Cancel': function() {
				$(this).dialog('close');
			}
		},
		close: function() {
			$(this).dialog('destroy');
			$(this).remove();

		},
	});
}