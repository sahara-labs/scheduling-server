 
/**
 * SAHARA Web Interface
 *
 * User interface to Sahara Remote Laboratory system.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, Michael Diponio
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
 * @date 17th September 2011
 */

/* ----------------------------------------------------------------------------
 * -- Tabs / Page setup.                                                     --
 * ---------------------------------------------------------------------------- */

function loadFederationTab(tab)
{
	$.get(
			"/federation/" + tab,
			null,
			function(response) {
				$("#tabcontents").empty().append(response);
				
				$("#lefttabbar .selectedtab").removeClass("selectedtab").addClass("notselectedtab");
				$("#" + tab + "tab").removeClass("notselectedtab").addClass("selectedtab");
				
				setupFederationTab(tab);
			}
	);
}

function setupFederationTab(tab)
{
	switch (tab)
	{
	case 'sites':
		$('#fedsites .fedsitetitle').click(function() {
			var nd = $(this).next('.fedsite'),
			    op = $('#fedsites .sitedisplayed');
			
			if (nd.css('display') == 'none') 
			{
				op.removeClass('sitedisplayed').next('.fedsite').slideUp();
				op.children('span').removeClass('ui-icon-circle-arrow-s').addClass('ui-icon-circle-arrow-e');
				
				$(this).addClass('sitedisplayed')
					.children('span').removeClass('ui-icon-circle-arrow-e').addClass('ui-icon-circle-arrow-s');
				
				nd.slideDown();
			} else {
				op.removeClass('sitedisplayed').next('.fedsite').slideUp();
				op.children('span').removeClass('ui-icon-circle-arrow-s').addClass('ui-icon-circle-arrow-e');
			}
		});
		break;
	}
}

$(document).ready(function() {
	/* Tool tip hovers. */
	var ttStates = new Object();
	$(".fedtab").hover(function() {
		var id = $(this).attr("id"), 
		    $this = $(this);
		ttStates[id] = true;
		setTimeout(function() {
			if (ttStates[id])
			{
				$this.children('.tooltiphov').fadeIn();
			}
		}, 1000);
	}, function() {
		var id = $(this).attr("id");
		if (ttStates[id])
		{
			ttStates[id] = false;
			$(this).children('.tooltiphov').fadeOut();
		}
	});
	
	/* Contents height. */
	$('#contentspane').css('height', $(window).height() - 230);
	$(window).resize(function() { 
		$('#contentspane').css('height', $(window).height() - 230);
	});
	
	/* Setup default tab. */
	setupFederationTab('sites');
});

/* ----------------------------------------------------------------------------
 * -- Sites Tab.                                                             --
 * ---------------------------------------------------------------------------- */

function addSite()
{
	alert("Add site");
}

function saveConsumerAuth(id, guid)
{
	$.post(
		"/federation/saveauth",
		{
			"guid": guid,
			"viewer": ($("#" + id + "viewer:checked").length == 1 ? "true" : "false"),
			"requestor": ($("#" + id + "requestor:checked").length == 1 ? "true" : "false"),
			"authorizor": ($("#"+ id + "authorizor:checked").length == 1 ? "true" : "false"),
		},
		function(response) { /* Nothing for now. */ }
	);
}

function loadProviderResources(guid)
{
	alert("Provider resources: " + guid);
}


