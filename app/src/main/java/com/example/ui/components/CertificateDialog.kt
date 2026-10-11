package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.Course
import com.example.ui.theme.AcademyAccent
import com.example.ui.theme.AcademyAccentDark
import com.example.ui.theme.AcademyPrimary

@Composable
fun CertificateDialog(
  studentName: String,
  course: Course,
  onDismiss: () -> Unit,
  onDownload: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("certificate_dialog"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
      )
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Close button at top right
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        // CERTIFICATE BODY
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .border(
              BorderStroke(2.dp, AcademyAccent),
              RoundedCornerShape(8.dp)
            )
            .background(Color(0xFFFFFDF5))
            .padding(16.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Image(
              painter = painterResource(id = R.drawable.ic_academy_logo),
              contentDescription = "Deepak Academy Crest",
              modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
            )

            Text(
              text = "DEEPAK ACADEMY",
              fontWeight = FontWeight.Black,
              fontSize = 16.sp,
              letterSpacing = 1.sp,
              color = AcademyPrimary
            )

            Text(
              text = "CERTIFICATE OF EXCELLENCE",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = AcademyAccentDark,
              letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "This is proudly presented to",
              fontSize = 11.sp,
              color = Color.DarkGray
            )

            Text(
              text = studentName,
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1E293B),
              textAlign = TextAlign.Center
            )

            HorizontalDivider(
              modifier = Modifier.width(160.dp),
              thickness = 1.dp,
              color = AcademyAccent
            )

            Text(
              text = "for outstanding dedication and successfully completing",
              fontSize = 11.sp,
              color = Color.DarkGray,
              textAlign = TextAlign.Center
            )

            Text(
              text = course.title,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = AcademyPrimary,
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Bottom
            ) {
              Column(horizontalAlignment = Alignment.Start) {
                Text(
                  text = "Issue Date: Oct 2026",
                  fontSize = 10.sp,
                  color = Color.Gray
                )
                Text(
                  text = "Cert ID: DA-CERT-8842",
                  fontSize = 10.sp,
                  color = Color.Gray
                )
              }

              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = "Deepak Kumar",
                  fontSize = 14.sp,
                  fontStyle = FontStyle.Italic,
                  fontWeight = FontWeight.Bold,
                  color = AcademyPrimary
                )
                Text(
                  text = "Er. Deepak Kumar",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = Color.Black
                )
                Text(
                  text = "Founder & Lead Mentor",
                  fontSize = 9.sp,
                  color = Color.Gray
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Button(
            onClick = onDownload,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = AcademyPrimary)
          ) {
            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Download PDF")
          }

          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f)
          ) {
            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Share")
          }
        }
      }
    }
  }
}
