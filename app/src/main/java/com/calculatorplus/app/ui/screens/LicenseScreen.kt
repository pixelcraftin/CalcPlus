package com.calculatorplus.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.calculatorplus.app.ui.theme.GrayText

@Composable
fun LicenseScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val gplPreamble = """
        GNU GENERAL PUBLIC LICENSE
        Version 3, 29 June 2007

        Copyright (C) 2007 Free Software Foundation, Inc. <https://fsf.org/>
        Everyone is permitted to copy and distribute verbatim copies
        of this license document, but changing it is not allowed.

        Preamble
        The GNU General Public License is a free, copyleft license for
        software and other kinds of works.

        The licenses for most software and other practical works are designed
        to take away your freedom to share and change the works.  By contrast,
        the GNU General Public License is intended to guarantee your freedom to
        share and change all versions of a program--to make sure it remains free
        software for all its users.  We, the Free Software Foundation, use the
        GNU General Public License for most of our software; it applies also to
        any other work released this way by its authors.  You can apply it to
        your programs, too.

        When we speak of free software, we are referring to freedom, not
        price.  Our General Public Licenses are designed to make sure that you
        have the freedom to distribute copies of free software (and charge for
        them if you wish), that you receive source code or can get it if you
        want it, that you can change the software or use pieces of it in new
        free programs, and that you know you can do these things.

        To protect your rights, we need to prevent others from denying you
        these rights or asking you to surrender the rights.  Therefore, you have
        certain responsibilities if you distribute copies of the software, or if
        you modify it: responsibilities to respect the freedom of others.

        For example, if you distribute copies of such a program, whether
        gratis or for a fee, you must give the recipients all the freedoms that
        you received.  You must make sure that they, too, receive or can get the
        source code.  And you must show them these terms so they know their
        rights.

        Developers that use the GNU GPL protect your rights with two steps:
        (1) assert copyright on the software, and (2) offer you this License
        giving you legal permission to copy, distribute and/or modify it.

        TERMS AND CONDITIONS

        0. Definitions.
        "This License" refers to version 3 of the GNU General Public License.
        "Copyright" also means copyright-like laws that apply to other kinds of works, such as semiconductor masks.
        "The Program" refers to any copyrightable work licensed under this License. Each licensee is addressed as "you". "Licensees" and "recipients" may be individuals or organizations.

        1. Source Code.
        The "source code" for a work means the preferred form of the work for making modifications to it. "Object code" means any non-source form of a work.
        
        2. Basic Permissions.
        All rights granted under this License are granted for the term of copyright on the Program, and are irrevocable provided the stated conditions are met. This License explicitly affirms your unlimited permission to run the unmodified Program. 

        3. Protects Users' Legal Rights From Anti-Circumvention Law.
        No covered work shall be deemed part of an effective technological measure under any applicable law fulfilling obligations under article 11 of the WIPO copyright treaty.

        4. Conveying Verbatim Copies.
        You may convey verbatim copies of the Program's source code as you receive it, in any medium, provided that you conspicuously and appropriately publish on each copy an appropriate copyright notice.

        5. Conveying Modified Source Versions.
        You may convey a work based on the Program, or the modifications to produce it from the Program, in the form of source code under the terms of section 4, provided that you also meet all of these conditions:
        a) The work must carry prominent notices stating that you modified it.
        b) The work must carry prominent notices stating that it is released under this License.

        6. Conveying Non-Source Forms.
        You may convey a covered work in object code form under the terms of sections 4 and 5, provided that you also convey the machine-readable Corresponding Source under the terms of this License.

        7. Additional Terms.
        "Additional permissions" are terms that supplement the terms of this License by making exceptions from one or more of its conditions.

        8. Termination.
        You may not propagate or modify a covered work except as expressly provided under this License. Any attempt otherwise to propagate or modify it is void.

        9. Acceptance Not Required for Having Copies.
        You are not required to accept this License in order to receive or run a copy of the Program.

        10. Automatic Licensing of Downstream Recipients.
        Each time you convey a covered work, the recipient automatically receives a license from the original licensors, to run, modify and propagate that work, subject to this License.

        NO WARRANTY
        THERE IS NO WARRANTY FOR THE PROGRAM, TO THE EXTENT PERMITTED BY APPLICABLE LAW. EXCEPT WHEN OTHERWISE STATED IN WRITING.
    """.trimIndent()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Inline Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(56.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "GPL V3 LICENSE",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = com.calculatorplus.app.ui.theme.PlusJakartaSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = gplPreamble,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
