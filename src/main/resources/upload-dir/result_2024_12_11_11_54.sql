USE [DoctorEleks v1]
GO

INSERT INTO [dbo].[Premium](
[sl_id]
, [UserShortName]
, [DepartmentName]
, [DepartmentID]
, [UserPosition]
, [EmploymentStartDate]
, [Employment]
, [EmploymentPart]
, [EmploymentPosition]
, [HourCoefficient]
, [NightHours]
, [Point]
, [PointValue]
, [BasicPremium]
, [HospNSZU_Premium]
, [CountEMR_stationary]
, [AmblNSZU_Premium]
, [SumForAmlPackage]
, [CountEMR_priorityService]
, [OneDaySurgery]
, [CountEMR_oneDaySurgery]
, [OtherPremium]
, date)
SELECT '2159', N'Ігнатишак Олександра Михайлівна', N'Інфекційне відділення', 3, N'лікар-інфекціоніст дитячий', '1998-12-01', 0,25, 0,25, 0,25, 1.187531, 16.0, 0, 0, 720.0, 0.0, 0, 0.0, 0.0, 0, 0.0, 0, 720.0, GETDATE()
UNION ALL 
SELECT '845', N'Ігнатишак Олександра Михайлівна', N'Інфекційне відділення', 3, N'лікар-інфекціоніст', '1998-12-01', 0,75, 0,75, 0,75, 1.0060197, 40.0, 0, 0, 2160.0, 0.0, 0, 0.0, 0.0, 0, 0.0, 0, 2160.0, GETDATE()
UNION ALL 
